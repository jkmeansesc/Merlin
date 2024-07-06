package org.haifan.merlin.interceptors;

import okhttp3.*;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.haifan.merlin.utils.JsonPrinter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Set;

/**
 * An interceptor for securely logging HTTP requests and responses.
 * This interceptor masks sensitive data and prettifies JSON content.
 */
public class SecureLoggingInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(SecureLoggingInterceptor.class);
    private static final Set<String> SENSITIVE_HEADERS = Set.of("Authorization", "Cookie", "Set-Cookie");

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logSecurely(request);
        long startTime = System.nanoTime();
        logger.info("");
        Response response = chain.proceed(request);
        long endTime = System.nanoTime();
        logSecurely(response, endTime - startTime);
        return response;
    }

    private void logSecurely(Request request) throws IOException {
        logger.info("Sending request to: {}", request.url());
        logger.debug("Request method: {}", request.method());
        logger.debug("Request Headers:");
        logHeaders(request.headers());
        logRequestBody(request);
    }

    private void logSecurely(Response response, long duration) throws IOException {
        logger.info("Received response for {} in {}ms", response.request().url(), duration / 1_000_000);
        logger.debug("Response Code: {}", response.code());
        logger.debug("Response Headers:");
        logHeaders(response.headers());
        logResponseBody(response);
    }

    private void logRequestBody(Request request) throws IOException {
        RequestBody body = request.body();
        if (body == null) return;

        MediaType mediaType = body.contentType();
        if (mediaType != null && mediaType.type().equals("multipart")) {
            logger.debug("Request Body: [multipart form data]");
            return;
        }

        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        String bodyString = buffer.readUtf8();

        if (isJsonContent(mediaType)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Request Body:\n{}", JsonPrinter.print(bodyString));
            }
        } else {
            logger.debug("Request Body: [binary data]");
            logger.trace("Raw Request Body:\n{}", bodyString);
        }
    }

    private void logResponseBody(Response response) throws IOException {
        ResponseBody body = response.body();
        if (body == null) return;

        MediaType mediaType = body.contentType();
        if (mediaType != null && mediaType.type().equals("multipart")) {
            logger.debug("Response Body: [multipart form data]");
            return;
        }

        // Use peekBody with a reasonable maximum size to avoid reading the entire response into memory
        ResponseBody peekBody = response.peekBody(1024L * 1024); // 1MB limit
        String bodyString = peekBody.string();

        if (isJsonContent(mediaType)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Response Body:\n{}", JsonPrinter.print(bodyString));
            }
        } else {
            logger.debug("Response Body: [binary data]");
            logger.trace("Raw Response Body:\n{}", bodyString);
        }
    }

    private void logHeaders(Headers headers) {
        headers.forEach(header -> {
            String headerName = header.getFirst();
            String headerValue = SENSITIVE_HEADERS.contains(headerName) ?
                    maskSensitiveData(header.getSecond()) : header.getSecond();
            logger.debug("{}: {}", headerName, headerValue);
        });
    }

    private String maskSensitiveData(String data) {
        if (data == null || data.length() < 4) {
            return "****";
        }
        return data.substring(0, 4) + "****";
    }

    private boolean isJsonContent(MediaType mediaType) {
        return mediaType != null &&
                (mediaType.subtype().toLowerCase().contains("json") ||
                        mediaType.subtype().equalsIgnoreCase("javascript"));
    }
}
