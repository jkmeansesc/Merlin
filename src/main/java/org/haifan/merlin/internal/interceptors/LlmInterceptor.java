package org.haifan.merlin.internal.interceptors;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Abstract interceptor for adding authentication headers to HTTP requests for Large Language Models (LLMs).
 * This class provides a base implementation for intercepting HTTP requests and modifying them to include
 * authentication tokens.
 */
public abstract class LlmInterceptor implements Interceptor {
    protected final String token;

    protected LlmInterceptor(String token) {
        this.token = token;
    }

    protected LlmInterceptor() {
        this.token = null;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request newRequest = addAuthHeader(originalRequest);
        return chain.proceed(newRequest);
    }

    protected abstract Request addAuthHeader(Request originalRequest);
}
