package org.haifan.merlin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.haifan.merlin.config.LlmConfig;
import org.haifan.merlin.interceptors.LlmInterceptor;
import org.haifan.merlin.interceptors.SLF4JHttpLogger;
import org.haifan.merlin.utils.DefaultObjectMapper;
import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.CompletableFuture;

/**
 * TODO: add javadoc
 */
@Slf4j
public abstract class LlmService {

    protected final Retrofit retrofit;
    protected final LlmConfig llmConfig;

    protected LlmService(LlmConfig llmConfig, LlmInterceptor llmInterceptor) {
        this.llmConfig = llmConfig;
        log.info("Initializing LlmService with base URL: {}", llmConfig.getBaseUrl());
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new SLF4JHttpLogger());
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        logging.redactHeader("Authorization");
        logging.redactHeader("x-goog-api-key");

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(llmInterceptor)
                .addNetworkInterceptor(logging)
                .build();

        ObjectMapper mapper = DefaultObjectMapper.create();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(llmConfig.getBaseUrl())
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();
        log.info("LlmService initialized");
    }

    protected <T> CompletableFuture<T> call(Call<T> call) {
        CompletableFuture<T> future = new CompletableFuture<>();
        log.info("Calling");
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
                if (response.isSuccessful()) {
                    log.info("Call successful");
                    future.complete(response.body());
                } else {
                    future.completeExceptionally(new Exception("API call failed: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
                log.error("API call failed due to exception", t);
                future.completeExceptionally(t);
            }
        });
        return future;
    }
}
