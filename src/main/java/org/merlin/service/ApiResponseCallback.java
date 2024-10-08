package org.merlin.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Callback implementation for handling API responses and completing a CompletableFuture.
 *
 * @param <T> the type of the response body
 */
@Slf4j
public class ApiResponseCallback<T> implements Callback<T> {
    private final CompletableFuture<T> future;

    /**
     * Constructs an ApiResponseCallback with the provided CompletableFuture.
     *
     * @param future the CompletableFuture to complete with the API response
     */
    public ApiResponseCallback(CompletableFuture<T> future) {
        this.future = future;
    }

    /**
     * Called when the API response is received.
     *
     * @param call     the Retrofit call that was executed
     * @param response the response received from the API
     */
    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (response.isSuccessful()) {
            log.info("Call successful with status code: {}", response.code());
            if (response.body() == null) {
                log.warn("Response body is null for successful response");
                future.complete(null);
            } else {
                future.complete(response.body());
            }
        } else {
            log.error("Call successful but with status code {}", response.code());
            try {
                String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                log.error("Error body: {}", errorBody);
            } catch (IOException e) {
                log.error("Failed to read error body", e);
            }
            future.completeExceptionally(new HttpException(response));
        }
    }

    /**
     * Called when the API call fails.
     *
     * @param call the Retrofit call that was executed
     * @param t    the throwable that caused the failure
     */
    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        log.error("API call failed due to exception", t);
        if (t instanceof IOException) {
            log.error("Network or conversion error", t);
        } else {
            log.error("Unexpected error", t);
        }
        future.completeExceptionally(t);
    }
}
