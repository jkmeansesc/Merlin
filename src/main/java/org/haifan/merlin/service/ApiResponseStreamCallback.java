package org.haifan.merlin.service;

import io.reactivex.rxjava3.core.FlowableEmitter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Function;

@Slf4j
public class ApiResponseStreamCallback<T> implements Callback<ResponseBody> {
    private final FlowableEmitter<T> emitter;
    private final Function<String, T> chunkParser;
    private final LlmService service;

    public ApiResponseStreamCallback(FlowableEmitter<T> emitter, Function<String, T> chunkParser, LlmService service) {
        this.emitter = emitter;
        this.chunkParser = chunkParser;
        this.service = service;
    }

    @Override
    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
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
            String line;
            while ((line = reader.readLine()) != null && !emitter.isCancelled()) {
                String chunk = service.parseStreamLine(line);
                T parsedChunk = chunkParser.apply(chunk);
                emitter.onNext(parsedChunk);
            }
            emitter.onComplete();
            log.info("Stream successful");
        } catch (Throwable e) {
            emitter.onError(e);
        }
    }

    @Override
    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
        emitter.onError(t);
    }
}
