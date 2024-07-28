package org.haifan.merlin.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.logging.HttpLoggingInterceptor;
import org.haifan.merlin.internal.constants.Provider;
import org.haifan.merlin.internal.utils.ApiKeyManager;

import java.time.Duration;

/**
 * Configuration class for setting up Large Language Model (LLM) connections.
 * This class holds configuration details such as the provider, base URL, token, timeout, and log level.
 * It also provides methods to retrieve the appropriate HTTP log level based on the configured log level.
 */
@Slf4j
@Data
public class LlmConfig {
    private final Provider provider;
    private final String baseUrl;
    private final String token;
    private Duration timeOut;
    private Level logLevel;

    private static final Level DEFAULT_LOG_LEVEL = Level.BASIC;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    HttpLoggingInterceptor.Level getHttpLogLevel() {
        return switch (logLevel) {
            case BASIC -> HttpLoggingInterceptor.Level.BASIC;
            case HEADERS -> HttpLoggingInterceptor.Level.HEADERS;
            case BODY -> HttpLoggingInterceptor.Level.BODY;
            default -> HttpLoggingInterceptor.Level.NONE;
        };
    }

    public LlmConfig(Provider provider, String baseUrl, String token) {
        log.info("Initializing config for {}", provider.name());
        this.provider = provider;

        if (token != null) {
            log.debug("Using supplied api key directly");
            this.token = token;
        } else {
            if (!Provider.OLLAMA.name().equals(provider.name())) {
                log.debug("Reading api key from environment");
            }
            this.token = ApiKeyManager.getApiKey(provider.name());
        }

        this.baseUrl = baseUrl;
        log.info("Config initialized");

        this.logLevel = DEFAULT_LOG_LEVEL;
        this.timeOut = DEFAULT_TIMEOUT;
    }

    public enum Level {
        NONE,
        BASIC,
        HEADERS,
        BODY
    }
}
