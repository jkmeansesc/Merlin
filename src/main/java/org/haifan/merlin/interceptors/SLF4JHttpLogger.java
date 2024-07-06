package org.haifan.merlin.interceptors;

import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SLF4JHttpLogger implements HttpLoggingInterceptor.Logger {
    private static final Logger logger = LoggerFactory.getLogger("OkHttpClient");

    @Override
    public void log(@NotNull String message) {
        if (message.startsWith("--> ")) {
            logger.info("Sending request: {}", message);
        } else if (message.startsWith("<-- ")) {
            logger.info("Received response: {}", message);
        } else {
            logger.debug(message);
        }
    }
}
