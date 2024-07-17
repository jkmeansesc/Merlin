package org.haifan.merlin.interceptors;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.haifan.merlin.constants.Provider;

import java.util.Objects;

/**
 * TODO: add javadoc
 */

@Slf4j
public class OpenAiInterceptor extends LlmInterceptor {

    public OpenAiInterceptor(String apiKey) {
        super(apiKey);
        Objects.requireNonNull(apiKey, "OpenAI API key required");
    }

    @Override
    protected Request addAuthHeader(Request originalRequest) {
        log.info("Adding auth header for {}", Provider.OPENAI);
        return originalRequest.newBuilder()
                .header("Authorization", "Bearer " + apiKey)
                .build();
    }
}
