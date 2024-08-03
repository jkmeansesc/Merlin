package org.merlin.internal.interceptors;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.merlin.internal.constants.Provider;

import java.util.Objects;

@Slf4j
public class GeminiInterceptor extends AuthenticationInterceptor {

    public GeminiInterceptor(String token) {
        super(token);
        Objects.requireNonNull(token, "Google Gemini API key required");
    }

    @Override
    protected Request addAuthHeader(Request originalRequest) {
        log.info("Adding auth header for {}", Provider.GOOGLE_GEMINI);
        return originalRequest.newBuilder()
                .header("x-goog-api-key", Objects.requireNonNull(token))
                .build();
    }
}
