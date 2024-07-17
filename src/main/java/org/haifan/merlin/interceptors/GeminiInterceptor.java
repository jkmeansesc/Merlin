package org.haifan.merlin.interceptors;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.haifan.merlin.constants.Provider;

import java.util.Objects;

@Slf4j
public class GeminiInterceptor extends LlmInterceptor {

    public GeminiInterceptor(String apiKey) {
        super(apiKey);
        Objects.requireNonNull(apiKey, "Google Gemini API key required");
    }

    @Override
    protected Request addAuthHeader(Request originalRequest) {
        log.info("Adding auth header for {}", Provider.GOOGLE_GEMINI);
        return originalRequest.newBuilder()
                .header("x-goog-api-key", apiKey)
                .build();
    }
}
