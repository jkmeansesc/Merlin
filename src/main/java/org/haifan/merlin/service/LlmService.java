package org.haifan.merlin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.haifan.merlin.internal.interceptors.LlmInterceptor;
import org.haifan.merlin.internal.interceptors.SLF4JHttpLogger;
import org.haifan.merlin.internal.utils.DefaultObjectMapper;
import org.jetbrains.annotations.NotNull;

import retrofit2.*;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * TODO: add javadoc
 */
@Slf4j
@Getter
public abstract class LlmService {

    protected final Retrofit retrofit;
    protected final LlmConfig config;
    protected final OkHttpClient client;
    protected final ObjectMapper mapper;

    protected LlmService(LlmConfig config, LlmInterceptor llmInterceptor) {

        log.info("Initializing LlmService with base URL: {}", config.getBaseUrl());
        this.config = config;

        log.debug("Creating default OkHttpClient");
        this.client = defaultOkHttpClient(llmInterceptor);

        log.debug("Creating default ObjectMapper");
        this.mapper = DefaultObjectMapper.create();

        log.debug("Creating default Retrofit instance");
        this.retrofit = defaultRetrofit();

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
                    log.error("Call successful but with status code {}", response.code());
                    future.completeExceptionally(new HttpException(response));
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

    protected <T> Flowable<T> stream(Call<ResponseBody> call, Function<String, T> chunkParser) {
        return Flowable.<T>create(emitter -> {
                    log.info("Streaming");
                    call.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                            processResponse(response, emitter, chunkParser);
                        }

                        @Override
                        public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable e) {
                            emitter.onError(e);
                        }
                    });
                }, io.reactivex.rxjava3.core.BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io());
    }

    private <T> void processResponse(Response<ResponseBody> response, FlowableEmitter<T> emitter, Function<String, T> chunkParser) {
        if (!response.isSuccessful()) {
            log.error("Stream successful but with status code {}", response.code());
            emitter.onError(new HttpException(response));
            return;
        }

        ResponseBody body = response.body();
        if (body == null) {
            log.error("Stream failed due to empty body");
            emitter.onError(new IOException("Null response body"));
            return;
        }

        try (BufferedReader reader = new BufferedReader(body.charStream())) {
            log.info("Got streaming response, start parsing");
            processStreamLines(reader, emitter, chunkParser);
            log.info("Stream successful");
        } catch (Throwable e) {
            emitter.onError(e);
        }
    }

    private <T> void processStreamLines(BufferedReader reader, FlowableEmitter<T> emitter, Function<String, T> chunkParser) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !emitter.isCancelled()) {
            String chunk = parseStreamLine(line);
            if (chunk != null) {
                T parsedChunk = chunkParser.apply(chunk);
                emitter.onNext(parsedChunk);
            }
        }
        emitter.onComplete();
    }

    private OkHttpClient defaultOkHttpClient(LlmInterceptor llmInterceptor) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new SLF4JHttpLogger());
        logging.setLevel(this.config.getHttpLogLevel());
        logging.redactHeader("Authorization");
        logging.redactHeader("x-goog-api-key");

        long timeoutMillis = this.config.getTimeOut().toMillis();

        return new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS))
                .readTimeout(timeoutMillis, TimeUnit.MILLISECONDS)
                .addInterceptor(llmInterceptor)
                .addNetworkInterceptor(logging)
                .build();
    }

    private Retrofit defaultRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Objects.requireNonNull(this.config).getBaseUrl())
                .client(this.client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();
    }

    protected abstract String parseStreamLine(String line);
}