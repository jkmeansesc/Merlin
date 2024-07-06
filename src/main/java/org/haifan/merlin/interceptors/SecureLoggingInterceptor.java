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
 * TODO: add javadoc
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
        Response response = chain.proceed(request);
        long endTime = System.nanoTime();
        logSecurely(response, endTime - startTime);
        return response;
    }

    private void logSecurely(Request request) throws IOException {
        logger.info("Sending request to: {}", request.url());
        logger.debug("Request method: {}", request.method());
        logHeaders(request.headers());
        if (request.body() != null) {
            Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            String body = buffer.readUtf8();
            if (logger.isDebugEnabled()) {
                logger.debug("Request Body:\n{}", JsonPrinter.print(body));
            }
        }
    }

    private void logSecurely(Response response, long duration) throws IOException {
        logger.info("Received response for {} in {}ms", response.request().url(), duration / 1_000_000);
        logger.debug("Response Code: {}", response.code());
        logHeaders(response.headers());
        if (response.body() != null && logger.isDebugEnabled()) {
            String body = response.peekBody(Long.MAX_VALUE).string();
            logger.debug("Response Body:\n{}", JsonPrinter.print(body));
        }
    }

    private void logHeaders(Headers headers) {
        logger.debug("Headers:");
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
}
