package com.ts4.customer.data.utils.Elastic;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.AppenderBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ts4.customer.data.utils.logs.TrazaPrincipal;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import org.apache.hc.client5.http.classic.methods.HttpPost;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.*;

public class ElasticHttpAppender extends AppenderBase<ILoggingEvent> {

    private TrazaPrincipal trazaPrincipal;
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final ObjectMapper mapper = new ObjectMapper();
    private ExecutorService executorService;
    private int threadPoolSize = 10;
    private int queueSize = 100000;
    private boolean asyncMode = true;

    private String elasticUrl;
    private String apiKey;
    private boolean includeFullStackTrace = true;
    private boolean includeCallerData = true;
    private boolean includeOtelTraces = true;
    private String applicationName = "mercurio-utils";

    private static final Map<String, ConcurrentHashMap<String, Object>> traceCorrelationMap = new ConcurrentHashMap<>();
    private static final Map<String, Long> processedTraceIds = new ConcurrentHashMap<>();
    private long traceExpirationMillis = 5 * 60 * 1000;
    private long cleanupIntervalMillis = 10 * 60 * 1000;
    private long lastCleanupTime = System.currentTimeMillis();

    @Override
    public void start() {
        if (elasticUrl == null || elasticUrl.isEmpty()) {
            addError("No elasticUrl provided for ElasticHttpAppender");
            return;
        }
        if (apiKey == null || apiKey.isEmpty()) {
            addError("No apiKey provided for ElasticHttpAppender");
            return;
        }

        if (asyncMode) {
            executorService = Executors.newFixedThreadPool(threadPoolSize);
        }

        addInfo("Starting ElasticHttpAppender with URL: " + elasticUrl);
        super.start();
    }

    // Setters...
    public void setElasticUrl(String elasticUrl) {
        this.elasticUrl = elasticUrl;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setIncludeFullStackTrace(boolean includeFullStackTrace) {
        this.includeFullStackTrace = includeFullStackTrace;
    }

    public void setIncludeCallerData(boolean includeCallerData) {
        this.includeCallerData = includeCallerData;
    }

    public void setIncludeOtelTraces(boolean includeOtelTraces) {
        this.includeOtelTraces = includeOtelTraces;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setTraceExpirationMillis(long traceExpirationMillis) {
        this.traceExpirationMillis = traceExpirationMillis;
    }

    public void setCleanupIntervalMillis(long cleanupIntervalMillis) {
        this.cleanupIntervalMillis = cleanupIntervalMillis;
    }

    public void setAsyncMode(boolean asyncMode) {
        this.asyncMode = asyncMode;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    @Override
    protected void append(ILoggingEvent event) {

        if (!isStarted()) {
            return;
        }
        if (includeCallerData && event.getCallerData().length == 0) {
            event.getCallerData();
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCleanupTime > cleanupIntervalMillis) {
            cleanExpiredTraces(currentTime);
            lastCleanupTime = currentTime;
        }
        boolean isFeignEvent = event.getLoggerName().contains("FeignResponseInterceptor");
        if (isFeignEvent) {
            handleFeignEvent(event);
            return;
        }
        if (asyncMode) {
            executorService.submit(() -> {
                try {
                    processEventAsync(event);
                } catch (Exception e) {
                    addError("Exception while processing log event asynchronously", e);
                }
            });
        } else {
            try {
                processEventSync(event);
            } catch (Exception e) {
                addError("Exception while processing log event", e);
            }
        }
    }

    private void handleFeignEvent(ILoggingEvent event) {
        try {
            String correlationId = null;
            Map<String, String> mdc = event.getMDCPropertyMap();
            if (mdc != null && mdc.containsKey("trace_id")) {
                correlationId = mdc.get("trace_id");
            } else {
                Span currentSpan = Span.current();
                if (currentSpan != null && currentSpan.getSpanContext().isValid()) {
                    correlationId = currentSpan.getSpanContext().getTraceId();
                } else {
                    processEventSync(event);
                    return;
                }
            }
            ConcurrentHashMap<String, Object> correlationData = traceCorrelationMap.computeIfAbsent(
                    correlationId, k -> new ConcurrentHashMap<>());

            boolean isRequest = event.getFormattedMessage().contains("REQUEST:");
            boolean isResponse = event.getFormattedMessage().contains("RESPONSE:");

            if (isRequest) {
                correlationData.put("request", event);
                correlationData.put("timestamp", System.currentTimeMillis());

                if (correlationData.containsKey("response")) {
                    processRequestResponsePair(correlationId, correlationData);
                }
            } else if (isResponse) {
                correlationData.put("response", event);
                correlationData.put("timestamp", System.currentTimeMillis());

                // Si ya tenemos un request, procesar el par
                if (correlationData.containsKey("request")) {
                    processRequestResponsePair(correlationId, correlationData);
                }
            } else {
                processEventSync(event);
            }
        } catch (Exception e) {
            addError("Error processing Feign event", e);
            processEventSync(event);
        }
    }

    private void processRequestResponsePair(String correlationId, ConcurrentHashMap<String, Object> data) {
        try {
            ILoggingEvent requestEvent = (ILoggingEvent) data.get("request");
            ILoggingEvent responseEvent = (ILoggingEvent) data.get("response");

            ObjectNode rootNode = mapper.createObjectNode();

            Map<String, String> mdcMap = requestEvent.getMDCPropertyMap();

            rootNode.put("trace_id", correlationId);
            rootNode.put("span_id", requestEvent.getMDCPropertyMap().get("span_id"));
            rootNode.put("@timestamp", Instant.now().toString());
            rootNode.put("logger_name", "FeignRequestResponse");
            rootNode.put("level", "INFO");
            rootNode.put("thread_name", requestEvent.getThreadName());

            String combinedMessage = "FEIGN_COMBINED - Request: " + extractRequestDetails(requestEvent.getFormattedMessage()) +
                    " | Response: " + extractResponseDetails(responseEvent.getFormattedMessage());
            rootNode.put("message", combinedMessage);

            Integer statusCode = extractStatusCode(responseEvent.getFormattedMessage());
            if (statusCode != null) {
                rootNode.put("status_code", statusCode);
            }

            ObjectNode requestNode = rootNode.putObject("request");
            requestNode.put("raw_message", requestEvent.getFormattedMessage());
            requestNode.put("timestamp", Instant.ofEpochMilli(requestEvent.getTimeStamp()).toString());
            addRequestDetails(requestNode, requestEvent.getFormattedMessage());

            ObjectNode responseNode = rootNode.putObject("response");
            responseNode.put("raw_message", responseEvent.getFormattedMessage());
            responseNode.put("timestamp", Instant.ofEpochMilli(responseEvent.getTimeStamp()).toString());
            addResponseDetails(responseNode, responseEvent.getFormattedMessage());

            try {
                Span currentSpan = Span.current();
                if (currentSpan != null && currentSpan.getSpanContext().isValid()) {
                    SpanContext ctx = currentSpan.getSpanContext();
                    ObjectNode spanNode = rootNode.putObject("trace");
                    spanNode.put("trace_id", ctx.getTraceId());
                    spanNode.put("span_id", ctx.getSpanId());
                    spanNode.put("trace_flags", ctx.getTraceFlags().asHex());
                }
            } catch (Exception e) {
            }

            ObjectNode serviceNode = rootNode.putObject("service");
            serviceNode.put("name", applicationName);
            serviceNode.put("type", "feign_client");

            HttpPost post = new HttpPost(elasticUrl);
            post.setHeader("Authorization", "ApiKey " + apiKey);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(mapper.writeValueAsString(rootNode), StandardCharsets.UTF_8));

            sendHttpRequest(post);

            traceCorrelationMap.remove(correlationId);

        } catch (Exception e) {
            addError("Error processing Feign request-response pair", e);
        }
    }

    private Integer extractStatusCode(String message) {
        if (message == null) return null;
        try {
            int statusIdx = message.indexOf("Status:");
            if (statusIdx >= 0) {
                int statusStart = statusIdx + 7;
                int statusEnd = message.indexOf("\n", statusStart);
                if (statusEnd < 0) statusEnd = message.length();
                String statusText = message.substring(statusStart, statusEnd).trim();
                int spaceIdx = statusText.indexOf(' ');
                if (spaceIdx > 0) {
                    return Integer.parseInt(statusText.substring(0, spaceIdx));
                } else {
                    return Integer.parseInt(statusText);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    private String extractRequestDetails(String message) {
        if (message == null) return "";
        int requestIdx = message.indexOf("REQUEST:");
        if (requestIdx >= 0) {
            return message.substring(requestIdx).trim();
        }
        return message;
    }

    private String extractResponseDetails(String message) {
        if (message == null) return "";
        int responseIdx = message.indexOf("RESPONSE:");
        if (responseIdx >= 0) {
            return message.substring(responseIdx).trim();
        }
        return message;
    }

    private void addRequestDetails(ObjectNode requestNode, String message) {
        try {
            if (message.contains("URL:")) {
                int urlStart = message.indexOf("URL:") + 4;
                int urlEnd = message.indexOf("\n", urlStart);
                if (urlEnd > urlStart) {
                    requestNode.put("url", message.substring(urlStart, urlEnd).trim());
                }
            }

            if (message.contains("Method:")) {
                int methodStart = message.indexOf("Method:") + 7;
                int methodEnd = message.indexOf("\n", methodStart);
                if (methodEnd > methodStart) {
                    requestNode.put("method", message.substring(methodStart, methodEnd).trim());
                }
            }

            // Extraer headers si están disponibles
            if (message.contains("Headers:")) {
                int headersStart = message.indexOf("Headers:");
                int headersEnd = message.indexOf("\n\n", headersStart);
                if (headersEnd > headersStart) {
                    String headersText = message.substring(headersStart + 8, headersEnd).trim();
                    ObjectNode headersNode = requestNode.putObject("headers");

                    // Parsear headers línea por línea
                    String[] headerLines = headersText.split("\n");
                    for (String headerLine : headerLines) {
                        int separator = headerLine.indexOf(':');
                        if (separator > 0) {
                            String name = headerLine.substring(0, separator).trim();
                            String value = headerLine.substring(separator + 1).trim();
                            headersNode.put(name, value);
                        }
                    }
                }
            }
            if (message.contains("Body:")) {
                int bodyStart = message.indexOf("Body:") + 5;
                String body = message.substring(bodyStart).trim();
                requestNode.put("body", body);
            }
        } catch (Exception e) {
            requestNode.put("parsing_error", e.getMessage());
        }
    }

    private void addResponseDetails(ObjectNode responseNode, String message) {
        // Extraer y añadir detalles estructurados de la respuesta si es posible
        try {
            if (message.contains("Status:")) {
                int statusStart = message.indexOf("Status:") + 7;
                int statusEnd = message.indexOf("\n", statusStart);
                if (statusEnd > statusStart) {
                    String statusText = message.substring(statusStart, statusEnd).trim();
                    try {
                        int spaceIdx = statusText.indexOf(' ');
                        if (spaceIdx > 0) {
                            int statusCode = Integer.parseInt(statusText.substring(0, spaceIdx));
                            responseNode.put("status_code", statusCode);
                            responseNode.put("status_text", statusText.substring(spaceIdx + 1).trim());
                        } else {
                            responseNode.put("status", statusText);
                        }
                    } catch (NumberFormatException e) {
                        responseNode.put("status", statusText);
                    }
                }
            }
            if (message.contains("Headers:")) {
                int headersStart = message.indexOf("Headers:");
                int headersEnd = message.indexOf("\n\n", headersStart);
                if (headersEnd > headersStart) {
                    String headersText = message.substring(headersStart + 8, headersEnd).trim();
                    ObjectNode headersNode = responseNode.putObject("headers");
                    String[] headerLines = headersText.split("\n");
                    for (String headerLine : headerLines) {
                        int separator = headerLine.indexOf(':');
                        if (separator > 0) {
                            String name = headerLine.substring(0, separator).trim();
                            String value = headerLine.substring(separator + 1).trim();
                            headersNode.put(name, value);
                        }
                    }
                }
            }

            if (message.contains("Body:")) {
                int bodyStart = message.indexOf("Body:") + 5;
                String body = message.substring(bodyStart).trim();
                responseNode.put("body", body);

                if (body.trim().startsWith("{") || body.trim().startsWith("[")) {
                    try {
                        Object jsonBody = mapper.readValue(body, Object.class);
                        responseNode.putPOJO("body_json", jsonBody);
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
            responseNode.put("parsing_error", e.getMessage());
        }
    }

    private void processEventSync(ILoggingEvent event) {
        try {
            Map<String, String> mdcMap = event.getMDCPropertyMap();
            String spanId = mdcMap.get("span_id");
            String traceId = mdcMap.get("trace_id");

            boolean isTrazaPrincipal = event.getLoggerName().contains("TrazaPrincipal");

            processLogEvent(event);
            if (includeOtelTraces && traceId != null && spanId != null) {
                processOtelTrace(event, traceId, spanId);
            }

            if (isTrazaPrincipal && (traceId == null || spanId == null)) {
                Span currentSpan = Span.current();
                if (currentSpan != null && currentSpan.getSpanContext().isValid()) {
                    String currentTraceId = currentSpan.getSpanContext().getTraceId();
                    String currentSpanId = currentSpan.getSpanContext().getSpanId();
                    processOtelTrace(event, currentTraceId, currentSpanId);
                }
            }
        } catch (Exception e) {
            addError("Exception processing log event", e);
        }
    }
    private void processEventAsync(ILoggingEvent event) {
        processEventSync(event);
    }
    private void processLogEvent(ILoggingEvent event) throws IOException {
        HttpPost post = new HttpPost(elasticUrl);
        post.setHeader("Authorization", "ApiKey " + apiKey);
        post.setHeader("Content-Type", "application/json");
        String json = createJsonPayload(event);
        post.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

        sendHttpRequest(post);
    }
    private void processOtelTrace(ILoggingEvent event, String traceId, String spanId) {
        try {
            Long lastProcessed = processedTraceIds.get(traceId);
            long now = System.currentTimeMillis();

            if (lastProcessed != null && (now - lastProcessed) < traceExpirationMillis) {
                return;
            }
            processedTraceIds.put(traceId, now);
            String json = createTracePayload(traceId, spanId, event);
            HttpPost post = new HttpPost(elasticUrl);
            post.setHeader("Authorization", "ApiKey " + apiKey);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

            sendHttpRequest(post);
        } catch (Exception e) {
            addError("Error processing OpenTelemetry trace", e);
        }
    }


    private void cleanExpiredTraces(long currentTime) {
        processedTraceIds.entrySet().removeIf(entry ->
                (currentTime - entry.getValue()) > traceExpirationMillis);

        traceCorrelationMap.entrySet().removeIf(entry -> {
            ConcurrentHashMap<String, Object> data = entry.getValue();
            if (data.containsKey("timestamp")) {
                long timestamp = (long) data.get("timestamp");
                return (currentTime - timestamp) > traceExpirationMillis;
            }
            return true;
        });
    }

    private String createTracePayload(String traceId, String spanId, ILoggingEvent event) throws IOException {
        ObjectNode rootNode = mapper.createObjectNode();

        rootNode.put("trace_id", traceId);
        rootNode.put("span_id", spanId);
        rootNode.put("@timestamp", Instant.now().toString());
        rootNode.put("logger_name", event.getLoggerName());
        rootNode.put("level", event.getLevel().toString());
        rootNode.put("message", event.getFormattedMessage());

        Map<String, String> mdc = event.getMDCPropertyMap();
        if (mdc != null && !mdc.isEmpty()) {
            ObjectNode mdcNode = rootNode.putObject("mdc");
            for (Map.Entry<String, String> entry : mdc.entrySet()) {
                mdcNode.put(entry.getKey(), entry.getValue());
            }
        }

        ObjectNode serviceNode = rootNode.putObject("service");
        serviceNode.put("name", applicationName);
        serviceNode.put("type", "opentelemetry");

        return mapper.writeValueAsString(rootNode);
    }


    private String createJsonPayload(ILoggingEvent event) throws IOException {
        ObjectNode rootNode = mapper.createObjectNode();
        StackTraceElement caller2 = event.getCallerData()[1];
        String trace = null;
        String span = null;
        String statusCode0 = null;
        String request = null;
        String response = null;
        String auth = null;
        String dispositivo = null;
        String version = null;
        String customer_id = null;
        String systemTime = String.valueOf(LocalTime.now());
        try {
            JsonObject jsonLog = JsonParser.parseString(event.getFormattedMessage()).getAsJsonObject();
            trace = jsonLog.get("traceId").getAsString();
            span = jsonLog.get("spanId").getAsString();
            statusCode0 = jsonLog.get("statusCode").getAsString();
            request = jsonLog.get("request").getAsString();
            response = jsonLog.get("response").getAsString();
            auth = jsonLog.get("auth").getAsString();
            dispositivo = jsonLog.get("dispositivo").getAsString();
            version = jsonLog.get("version").getAsString();
            customer_id = jsonLog.get("customer_id").getAsString();
        } catch (Exception e) {
            addError(e.getMessage());
        }


        rootNode.put("level", event.getLevel().toString());
        rootNode.put("logger_name", event.getLoggerName());
        rootNode.put("thread_name", event.getThreadName());
        rootNode.put("trace_id", trace);
        rootNode.put("span_id", span);
        rootNode.put("@timestamp", Instant.ofEpochMilli(event.getTimeStamp()).toString());
        rootNode.put("statusCode", statusCode0);
        rootNode.put("trace_method", caller2.getMethodName());
        rootNode.put("request", request);
        rootNode.put("response", response);
        rootNode.put("auth", auth);
        rootNode.put("dispositivo", dispositivo);
        rootNode.put("version", version);
        rootNode.put("systemTime", systemTime);
        rootNode.put("customer_id", customer_id);

        String message = event.getFormattedMessage();
        if (message != null && message.contains("Status:")) {
            Integer statusCode = extractStatusCode(message);
            if (statusCode != null) {
                rootNode.put("status_code", statusCode);
            }
        }

        Map<String, String> mdc = event.getMDCPropertyMap();
        if (mdc != null && !mdc.isEmpty()) {
            ObjectNode mdcNode = rootNode.putObject("mdc");

            for (Map.Entry<String, String> entry : mdc.entrySet()) {
                mdcNode.put(entry.getKey(), entry.getValue());

                if (entry.getKey().equals("trace_id")) {
                    rootNode.put("trace_id", entry.getValue());
                }
                if (entry.getKey().equals("span_id")) {
                    rootNode.put("span_id", entry.getValue());
                }
                if (entry.getKey().equals("status_code") || entry.getKey().equals("statusCode")) {
                    try {
                        int code = Integer.parseInt(entry.getValue());
                        rootNode.put("status_code", code);
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }

        if (!rootNode.has("trace_id") || !rootNode.has("span_id")) {
            try {
                Span currentSpan = Span.current();
                if (currentSpan != null && currentSpan.getSpanContext().isValid()) {
                    SpanContext ctx = currentSpan.getSpanContext();

                    if (!rootNode.has("trace_id")) {
                        rootNode.put("trace_id", ctx.getTraceId());
                    }
                    if (!rootNode.has("span_id")) {
                        rootNode.put("span_id", ctx.getSpanId());
                    }

                    ObjectNode spanNode = rootNode.putObject("trace");
                    spanNode.put("trace_id", ctx.getTraceId());
                    spanNode.put("span_id", ctx.getSpanId());
                    spanNode.put("trace_flags", ctx.getTraceFlags().asHex());
                }
            } catch (Exception e) {
            }
        }

        if (includeCallerData && event.getCallerData() != null && event.getCallerData().length > 0) {
            StackTraceElement caller = event.getCallerData()[0];
            ObjectNode callerNode = rootNode.putObject("caller");
            callerNode.put("class", caller.getClassName());

            callerNode.put("method", caller.getMethodName());
            callerNode.put("file", caller.getFileName());
            callerNode.put("line", caller.getLineNumber());
        }

        if (event.getThrowableProxy() != null) {
            addExceptionInfo(rootNode, event.getThrowableProxy());
        }

        ObjectNode serviceNode = rootNode.putObject("service");
        serviceNode.put("name", applicationName);

        return mapper.writeValueAsString(rootNode);
    }

    private void addExceptionInfo(ObjectNode rootNode, IThrowableProxy throwableProxy) {
        ObjectNode errorNode = rootNode.putObject("error");
        errorNode.put("message", throwableProxy.getMessage());
        errorNode.put("class", throwableProxy.getClassName());
        StringBuilder stackTraceText = new StringBuilder();
        for (StackTraceElementProxy ste : throwableProxy.getStackTraceElementProxyArray()) {
            stackTraceText.append(ste.toString()).append("\n");
        }
        errorNode.put("stack_trace", stackTraceText.toString());
        if (includeFullStackTrace) {
            ArrayNode stackTraceArray = errorNode.putArray("stack_frames");
            for (StackTraceElementProxy ste : throwableProxy.getStackTraceElementProxyArray()) {
                StackTraceElement frame = ste.getStackTraceElement();
                ObjectNode stackFrame = stackTraceArray.addObject();
                stackFrame.put("class", frame.getClassName());
                stackFrame.put("method", frame.getMethodName());
                stackFrame.put("file", frame.getFileName());
                stackFrame.put("line", frame.getLineNumber());
                stackFrame.put("native", frame.isNativeMethod());
            }
        }
        if (throwableProxy.getCause() != null) {
            ObjectNode causesNode = errorNode.putObject("cause");
            addExceptionInfo(causesNode, throwableProxy.getCause());
        }
        if (throwableProxy.getSuppressed() != null && throwableProxy.getSuppressed().length > 0) {
            ArrayNode suppressedArray = errorNode.putArray("suppressed");
            for (IThrowableProxy suppressed : throwableProxy.getSuppressed()) {
                ObjectNode suppressedNode = suppressedArray.addObject();
                addExceptionInfo(suppressedNode, suppressed);
            }
        }
    }
    private void sendHttpRequest(HttpPost post) {
        Future<CloseableHttpResponse> resp = executorService.submit(() ->
                httpClient.execute(post)
        );
        try(CloseableHttpResponse response = resp.get(3,TimeUnit.SECONDS)){
        }
        catch (TimeoutException e) {
            resp.cancel(true);
            addError("Exception sending to ElasticSearch", e);
        }
        catch (Exception e) {
            addError("Exception sending to ElasticSearch", e);
        }
        finally {
            executorService.shutdown();
        }
    }

    @Override
    public void stop() {
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        super.stop();
    }
}