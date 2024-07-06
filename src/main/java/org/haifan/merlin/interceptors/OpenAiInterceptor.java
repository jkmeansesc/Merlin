package org.haifan.merlin.interceptors;

import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.haifan.merlin.constants.Provider;

import java.util.Objects;

/**
 * TODO: add javadoc
 */
public class OpenAiInterceptor extends LlmInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(OpenAiInterceptor.class);

    public OpenAiInterceptor(String apiKey) {
        super(apiKey);
        Objects.requireNonNull(apiKey, "OpenAI API key required");
    }

    @Override
    protected Request addAuthHeader(Request originalRequest) {
        logger.info("Adding auth header for {}", Provider.OPENAI);
        return originalRequest.newBuilder()
                .header("Authorization", "Bearer " + apiKey)
                .build();
    }
}
