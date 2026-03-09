package com.ts4.customer.data.utils.logs;

import feign.Client;
import feign.Request;
import feign.Response;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;


@Slf4j
public class FeignResponseInterceptor implements Client {

    private final Client delegate;
    private final Tracer tracer;

    public FeignResponseInterceptor(Client delegate, Tracer tracer) {
        this.delegate = delegate;
        this.tracer = tracer;
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        // Generar un ID de correlación único para esta solicitud/respuesta
        String correlationId = UUID.randomUUID().toString();
        String correlationIdSpan = UUID.randomUUID().toString();

        String traceId = null;
        String spanId = null;

        try{
            Span currentSpan = Span.current();
            if (currentSpan != null && currentSpan.getSpanContext().isValid()) {
                traceId = currentSpan.getSpanContext().getTraceId();
                spanId = currentSpan.getSpanContext().getSpanId();
                correlationIdSpan = spanId;
                correlationId = traceId;
            }

            MDC.put("trace_id", correlationId);
            MDC.put("span_id", spanId);

            String requestBody = "N/A";
            if (request.body() != null) {
                requestBody = new String(request.body(), StandardCharsets.UTF_8);
            }
            Span span = tracer.spanBuilder(request.requestTemplate().path())
                    .setAttribute("http.method", request.httpMethod().name())
                    .setAttribute("http.url", request.url())
                    .setAttribute("http.requestBody", requestBody)
                    .setAttribute("trace_id", correlationId)
                    .startSpan();

            for (Map.Entry<String, Collection<String>> header : request.headers().entrySet()) {
                String headerValue = String.join(",", header.getValue());
                span.setAttribute("header." + header.getKey(), headerValue);
            }

            try (Scope scope = span.makeCurrent()) {
                log.info("REQUEST: [{}] {} {} - Body: {}",
                        correlationId,
                        request.httpMethod(),
                        request.url(),
                        requestBody.length() > 100000 ? requestBody.substring(0, 100000) + "..." : requestBody);
                Response response = delegate.execute(request, options);


                String responseBody = "N/A";
                if (response.body() != null) {
                    byte[] bodyData = response.body().asInputStream().readAllBytes();
                    responseBody = new String(bodyData, StandardCharsets.UTF_8);
                    response = response.toBuilder()
                            .body(new ByteArrayInputStream(bodyData), bodyData.length)
                            .build();
                }

                if (response.status() >= 400) {
                    String message = String.format("Feign call returned error status: %d - Body: %s", response.status(), responseBody);
                    log.error("FEIGN ERROR: [{}] {}", correlationId, message);
                    byte[] bodyData = response.body().asInputStream().readAllBytes();

                }

                log.info("RESPONSE: [{}] Status: {} - Body: {}",
                        correlationId,
                        response.status(),
                        responseBody.length() > 100000 ? responseBody.substring(0, 10000) + "..." : responseBody);

                span.setAttribute("status_code", response.status());
                span.setAttribute("responseBody", responseBody);

                return response;
            } catch (Exception e) {
                span.recordException(e);
                log.error("ERROR: [{}] Error en solicitud: {}", correlationId, e.getMessage(), e);
                throw e;
            } finally {
                // Finalizar el span y limpiar el MDC
                span.end();
                MDC.remove("trace_id");
            }
        }catch(Exception e){
            log.error("ERROR: [{}] Error inesperado: {}", correlationId, e.getMessage(), e);
            throw e;
        }
    }
}