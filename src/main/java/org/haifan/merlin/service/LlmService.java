package org.haifan.merlin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
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

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * TODO: add javadoc
 */
@Slf4j
public abstract class LlmService {

    protected final Retrofit retrofit;
    protected final LlmConfig llmConfig;
    protected final OkHttpClient client;
    protected final ObjectMapper mapper;

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    protected LlmService(LlmConfig llmConfig, LlmInterceptor llmInterceptor) {

        this.llmConfig = llmConfig;
        log.info("Initializing LlmService with base URL: {}", llmConfig.getBaseUrl());

        this.client = defaultOkHttpClient(llmInterceptor);
        log.info("Creating default OkHttpClient");

        this.mapper = DefaultObjectMapper.create();
        log.info("Creating default ObjectMapper");

        this.retrofit = defaultRetrofit();
        log.info("Creating default Retrofit instance");

        log.info("LlmService initialized");
    }

    protected <T> CompletableFuture<T> call(Call<T> call) {
        CompletableFuture<T> future = new CompletableFuture<>();
        log.info("Calling");
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
                if (response.isSuccessful()) {
                    log.info("Call successful with status code: {}", response.code());
                    future.complete(response.body());
                } else {
                    log.info("Call successful but with status code {}", response.code());
                    log.info("Throwing LlmApiException with details. It is wrapped in a CompletionException");
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body found";
                        future.completeExceptionally(new LlmApiException("API call failed with status code: " + response.code() + "\n" + errorBody, response.code(), errorBody));
                    } catch (IOException e) {
                        future.completeExceptionally(new LlmApiException("API call failed with status code: " + response.code() + "\n" + "Failed to read error body", response.code(), "Failed to read error body"));
                    }
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

    private OkHttpClient defaultOkHttpClient(LlmInterceptor llmInterceptor) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new SLF4JHttpLogger());
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        logging.redactHeader("Authorization");
        logging.redactHeader("x-goog-api-key");

        // Read timeout from config, use default if not present
        long timeoutMillis = DEFAULT_TIMEOUT.toMillis();
        if (this.llmConfig != null && this.llmConfig.getConfig().has("time_out")) {
            try {
                timeoutMillis = this.llmConfig.getConfig().get("time_out").asInt() * 1000L;
            } catch (NumberFormatException e) {
                log.warn("Invalid time_out value in config. Using default timeout.", e);
            }
        }

        return new OkHttpClient.Builder().connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS)).readTimeout(timeoutMillis, TimeUnit.MILLISECONDS).addInterceptor(llmInterceptor).addNetworkInterceptor(logging).build();
    }

    private Retrofit defaultRetrofit() {
        return new Retrofit.Builder().baseUrl(this.llmConfig.getBaseUrl()).client(this.client).addConverterFactory(JacksonConverterFactory.create(mapper)).build();
    }

    public abstract JsonNode getConfig();

    public abstract Retrofit getRetrofit();

    public abstract OkHttpClient getOkHttpClient();

    public abstract ObjectMapper getObjectMapper();
}