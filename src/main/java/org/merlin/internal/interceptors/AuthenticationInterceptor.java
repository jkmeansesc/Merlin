package org.merlin.internal.interceptors;

import lombok.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Abstract interceptor for adding authentication headers to HTTP requests for Large Language Models (LLMs).
 * This class provides a base implementation for intercepting HTTP requests and modifying them to include
 * authentication tokens.
 */
public abstract class AuthenticationInterceptor implements Interceptor {
    protected final String token;

    protected AuthenticationInterceptor(String token) {
        this.token = token;
    }

    protected AuthenticationInterceptor() {
        this.token = null;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request newRequest = addAuthHeader(originalRequest);
        return chain.proceed(newRequest);
    }

    protected abstract Request addAuthHeader(Request originalRequest);
}
