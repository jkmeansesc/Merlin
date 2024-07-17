package org.haifan.merlin.interceptors;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * TODO: add javadoc
 */
public abstract class LlmInterceptor implements Interceptor {
    protected final String apiKey;

    protected LlmInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    protected LlmInterceptor() {
        this.apiKey = null;
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
