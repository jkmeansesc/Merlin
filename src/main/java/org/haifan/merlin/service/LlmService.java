package org.haifan.merlin.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;

import okhttp3.logging.HttpLoggingInterceptor;
import org.haifan.merlin.config.LlmConfig;
import org.haifan.merlin.interceptors.LlmInterceptor;
import org.haifan.merlin.interceptors.SLF4JHttpLogger;
import org.haifan.merlin.interceptors.SecureLoggingInterceptor;
import org.jetbrains.annotations.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.CompletableFuture;

/**
 * TODO: add javadoc
 */
public abstract class LlmService {

    private static final Logger logger = LoggerFactory.getLogger(LlmService.class);
    protected final Retrofit retrofit;
    protected final LlmConfig llmConfig;

    public abstract LlmConfig getConfig();

    protected LlmService(LlmConfig llmConfig, LlmInterceptor llmInterceptor) {
        this.llmConfig = llmConfig;
        logger.info("Initializing LlmService with base URL: {}", llmConfig.getBaseUrl());
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new SLF4JHttpLogger());
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        logging.redactHeader("Authorization");
        logging.redactHeader("Cookie");
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(llmInterceptor)
                .addNetworkInterceptor(logging)
//                .addNetworkInterceptor(new SecureLoggingInterceptor())
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        this.retrofit = new Retrofit.Builder()
                .baseUrl(llmConfig.getBaseUrl())
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();
        logger.info("LlmService initialized");
    }

    protected <T> CompletableFuture<T> call(Call<T> call) {
        CompletableFuture<T> future = new CompletableFuture<>();
        logger.info("Calling");
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
                if (response.isSuccessful()) {
                    logger.info("Call successful");
                    future.complete(response.body());
                } else {
                    future.completeExceptionally(new Exception("API call failed: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
                logger.error("API call failed due to exception", t);
                future.completeExceptionally(t);
            }
        });
        return future;
    }
}
