package org.merlin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.merlin.internal.interceptors.AuthenticationInterceptor;
import org.merlin.internal.interceptors.SLF4JHttpLogger;
import org.merlin.internal.utils.DefaultObjectMapper;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Abstract base class for interacting with Language Model Services via REST API.
 * Provides common setup for Retrofit, OkHttpClient, and ObjectMapper.
 */
@Slf4j
@Getter
public abstract class LlmService {

    protected final Retrofit retrofit;
    protected final LlmConfig config;
    protected final OkHttpClient client;
    protected final ObjectMapper mapper;

    /**
     * Constructs an LlmService with the provided configuration and authentication interceptor.
     *
     * @param config                    the configuration settings for the service
     * @param authenticationInterceptor the interceptor for handling authentication
     */
    protected LlmService(LlmConfig config, AuthenticationInterceptor authenticationInterceptor) {

        log.info("Initializing LlmService with base URL: {}", config.getBaseUrl());
        this.config = config;

        log.debug("Creating default OkHttpClient");
        this.client = defaultOkHttpClient(authenticationInterceptor);

        log.debug("Creating default ObjectMapper");
        this.mapper = DefaultObjectMapper.create();

        log.debug("Creating default Retrofit instance");
        this.retrofit = defaultRetrofit();

        log.info("LlmService initialized");
    }

    /**
     * Executes a synchronous call and returns a CompletableFuture that completes with the response.
     *
     * @param <T>  the type of the response
     * @param call the Retrofit call to execute
     * @return a CompletableFuture containing the response
     */
    protected <T> CompletableFuture<T> call(Call<T> call) {
        CompletableFuture<T> future = new CompletableFuture<>();
        log.info("Calling");
        call.enqueue(new ApiResponseCallback<>(future));
        return future;
    }

    /**
     * Executes a streaming call and returns a Flowable that emits parsed chunks of the response.
     *
     * @param <T>         the type of the parsed response chunks
     * @param call        the Retrofit call to execute
     * @param chunkParser a function to parse chunks of the response body
     * @return a Flowable emitting parsed response chunks
     */
    protected <T> Flowable<T> stream(Call<ResponseBody> call, Function<String, T> chunkParser) {
        log.info("Streaming");
        return Flowable.<T>create(emitter -> call.enqueue(new ApiResponseStreamCallback<>(emitter, chunkParser)), io.reactivex.rxjava3.core.BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io());
    }

    /**
     * Creates and configures the default OkHttpClient.
     *
     * @param authenticationInterceptor the interceptor for handling authentication
     * @return the configured OkHttpClient
     */
    private OkHttpClient defaultOkHttpClient(AuthenticationInterceptor authenticationInterceptor) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new SLF4JHttpLogger());
        logging.setLevel(this.config.getHttpLogLevel());
        logging.redactHeader("Authorization");
        logging.redactHeader("x-goog-api-key");

        long timeoutMillis = this.config.getTimeOut().toMillis();

        return new OkHttpClient.Builder().connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS)).readTimeout(timeoutMillis, TimeUnit.MILLISECONDS).addInterceptor(authenticationInterceptor).addNetworkInterceptor(logging).build();
    }

    /**
     * Creates and configures the default Retrofit instance.
     *
     * @return the configured Retrofit instance
     */
    private Retrofit defaultRetrofit() {
        return new Retrofit.Builder().baseUrl(Objects.requireNonNull(this.config).getBaseUrl()).client(this.client).addConverterFactory(JacksonConverterFactory.create(mapper)).build();
    }
}