package org.merlin.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.logging.HttpLoggingInterceptor;
import org.merlin.internal.constants.Provider;
import org.merlin.internal.utils.ApiKeyManager;

import java.time.Duration;

/**
 * Configuration class for Language Model Services.
 * Holds configuration settings such as provider, base URL, authentication token, timeout, and logging level.
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

    /**
     * Converts the log level to the corresponding HttpLoggingInterceptor.Level.
     *
     * @return the HttpLoggingInterceptor.Level corresponding to the log level
     */
    HttpLoggingInterceptor.Level getHttpLogLevel() {
        return switch (logLevel) {
            case BASIC -> HttpLoggingInterceptor.Level.BASIC;
            case HEADERS -> HttpLoggingInterceptor.Level.HEADERS;
            case BODY -> HttpLoggingInterceptor.Level.BODY;
            default -> HttpLoggingInterceptor.Level.NONE;
        };
    }

    /**
     * Constructs an LlmConfig with the provided provider, base URL, and token.
     * If the token is not provided, it attempts to read the API key from the environment.
     *
     * @param provider the provider of the language model service
     * @param baseUrl  the base URL of the language model service
     * @param token    the authentication token (optional)
     */
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

    /**
     * Enum representing the logging levels.
     */
    public enum Level {
        NONE,
        BASIC,
        HEADERS,
        BODY
    }
}
