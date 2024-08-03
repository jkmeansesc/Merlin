package org.merlin.internal.interceptors;

import lombok.NonNull;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SLF4JHttpLogger implements HttpLoggingInterceptor.Logger {
    private static final Logger logger = LoggerFactory.getLogger("OkHttpClient");

    @Override
    public void log(@NonNull String message) {
        if (message.startsWith("--> ")) {
            logger.info("Sending request: {}", message);
        } else if (message.startsWith("<-- ")) {
            logger.info("Received response: {}", message);
        } else {
            logger.debug(message);
        }
    }
}
