package org.haifan.merlin.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import org.haifan.merlin.config.LlmConfig;
import org.haifan.merlin.utils.JsonPrinter;
import org.jetbrains.annotations.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.CompletableFuture;

public abstract class LlmService {

    private static final Logger logger = LogManager.getLogger(LlmService.class);
    protected final Retrofit retrofit;
    protected final LlmConfig llmConfig;

    public abstract LlmConfig getConfig();

    protected LlmService(LlmConfig llmConfig) {
        this.llmConfig = llmConfig;
        logger.info("Initializing LlmService with base URL: {}", llmConfig.getBaseUrl());
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original
                    .newBuilder()
                    .header("Authorization", "Bearer " + llmConfig.getApiKey())
                    .method(original.method(), original.body())
                    .build();
            logger.debug("Sending request to: {}", request.url());
            return chain.proceed(request);
        }).build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(llmConfig.getBaseUrl())
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper()))
                .build();
        logger.info("LlmService initialized");
    }

    protected <T> CompletableFuture<T> executeCall(Call<T> call) {
        CompletableFuture<T> future = new CompletableFuture<>();
        logger.info("Calling");
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
                if (response.isSuccessful()) {
                    logger.info("API call successful, response code: {}", response.code());
                    if (logger.isDebugEnabled()) {
                        logger.debug("Response body:\n {}", JsonPrinter.print(response.body()));
                    }
                    future.complete(response.body());
                } else {
                    logger.error("API call failed, response code: {}", response.code());
                    logger.error("Error body:\n {}", JsonPrinter.print(response.body()));
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
