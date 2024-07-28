package org.haifan.merlin.service;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class ApiResponseCallback<T> implements Callback<T> {
    private final CompletableFuture<T> future;

    public ApiResponseCallback(CompletableFuture<T> future) {
        this.future = future;
    }

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
}