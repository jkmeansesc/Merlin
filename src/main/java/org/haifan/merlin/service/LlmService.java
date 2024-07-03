package org.haifan.merlin.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import org.haifan.merlin.config.LlmConfig;
import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.CompletableFuture;

public abstract class LlmService {

    protected final Retrofit retrofit;
    protected final LlmConfig llmConfig;

    public abstract LlmConfig getConfig();

    public LlmService(LlmConfig llmConfig) {
        this.llmConfig = llmConfig;
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original
                    .newBuilder()
                    .header("Authorization", "Bearer " + llmConfig.getApiKey())
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        }).build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(llmConfig.getBaseUrl())
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper()))
                .build();
    }

    protected <T> CompletableFuture<T> executeCall(Call<T> call) {
        CompletableFuture<T> future = new CompletableFuture<>();
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
                if (response.isSuccessful()) {
                    future.complete(response.body());
                } else {
                    future.completeExceptionally(new Exception("API call failed: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }
}
