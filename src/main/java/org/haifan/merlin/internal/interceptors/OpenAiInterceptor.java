package org.haifan.merlin.internal.interceptors;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.haifan.merlin.internal.constants.Provider;

import java.util.Objects;

@Slf4j
public class OpenAiInterceptor extends AuthenticationInterceptor {

    public OpenAiInterceptor(String token) {
        super(token);
        Objects.requireNonNull(token, "OpenAI API key required");
    }

    @Override
    protected Request addAuthHeader(Request originalRequest) {
        log.info("Adding auth header for {}", Provider.OPENAI);
        return originalRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();
    }
}
