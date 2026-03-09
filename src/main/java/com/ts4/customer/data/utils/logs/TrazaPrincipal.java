package com.ts4.customer.data.utils.logs;

import com.google.gson.Gson;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TrazaPrincipal {
    private static final Logger logger = LoggerFactory.getLogger(TrazaPrincipal.class);
    private final Tracer tracer;

    public TrazaPrincipal(Tracer tracer) {
        this.tracer = tracer;
    }

    public void trazaPrincipal(Map<String,Object> process, ResponseEntity<?> response,Span span){
        try {
            SpanContext spanContext = span.getSpanContext();
            String traceId = spanContext.getTraceId();
            String spanId = spanContext.getSpanId();

            MDC.clear();
            MDC.put("trace_id", traceId);
            MDC.put("span_id", spanId);

            String responseBody = "NA";
            try {
                if (response.getBody() != null) {
                    responseBody = new Gson().toJson(response.getBody());
                }
            } catch (Exception jsonException) {
                logger.warn("Error al serializar response body: {}", jsonException.getMessage());
                responseBody = response.getBody() != null ?
                        "Response body (no serializable): " + response.getBody().getClass().getSimpleName() :
                        "NA";
            }

            /*
            span.setAttribute("http.method", "POST");
            for (Map.Entry<String, Object> entry : process.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                span.setAttribute(key, value != null ? value.toString() : "N/A");
            }
            */

            span.setAttribute("http.statusCodeResponse", response.getStatusCodeValue());
            span.setAttribute("http.responsePrincipal", response.getBody()!=null ? new Gson().toJson(response.getBody()) : "NA");
            MDC.put("http.statusCodeResponse", String.valueOf(response.getStatusCodeValue()));
            MDC.put("http.responsePrincipal", responseBody);



            String request = "";
            String auth = "No required Authorization";
            String dispositivo = "";
            String version = "";
            String statusCode = response.getStatusCode().toString();
            String customer_id = "";


            try {
                request = process.get("http.requestPrincipal").toString();
                dispositivo = process.get("dispositivo").toString();
                version = process.get("version").toString();
                customer_id = process.get("customer_id").toString();

            }
            catch (Exception ignored)
            {

            }
            try
            {
                auth = process.get("Authorization").toString();
            }
            catch (Exception e)
            {
                logger.warn("Error parsing Authorization header: {}", "no authorization header");
            }

            Map<String, Object> logData = Map.of(
                    "traceId", traceId,
                    "spanId", spanId,
                    "statusCode", statusCode,
                    "request", request,
                    "response", responseBody,
                    "auth",auth,
                    "dispositivo", dispositivo,
                    "version", version,
                    "customer_id", customer_id
            );
            logger.info(new Gson().toJson(logData));

        } finally {
            span.end();
            MDC.clear();
        }
    }
}
