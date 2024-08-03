package org.haifan.merlin.service;

import io.reactivex.rxjava3.core.FlowableEmitter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Function;

/**
 * Callback implementation for handling streaming API responses and emitting parsed chunks to a FlowableEmitter.
 *
 * @param <T> the type of the parsed response chunks
 */
@Slf4j
public class ApiResponseStreamCallback<T> implements Callback<ResponseBody> {
    private final FlowableEmitter<T> emitter;
    private final Function<String, T> chunkParser;

    /**
     * Constructs an ApiResponseStreamCallback with the provided emitter and chunk parser.
     *
     * @param emitter     the FlowableEmitter to emit parsed chunks to
     * @param chunkParser a function to parse chunks of the response body
     */
    public ApiResponseStreamCallback(FlowableEmitter<T> emitter, Function<String, T> chunkParser) {
        this.emitter = emitter;
        this.chunkParser = chunkParser;
    }

    /**
     * Called when the API response is received.
     *
     * @param call     the Retrofit call that was executed
     * @param response the response received from the API
     */
    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
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
                String chunk = parseStreamLine(line);
                if (chunk != null && !chunk.isEmpty()) {
                    T parsedChunk = chunkParser.apply(chunk);
                    emitter.onNext(parsedChunk);
                }
            }
            log.debug("No more chunks, completing");
            emitter.onComplete();
            log.info("Stream successful");
        } catch (Throwable e) {
            emitter.onError(e);
        }
    }

    /**
     * Called when the API call fails.
     *
     * @param call the Retrofit call that was executed
     * @param t    the throwable that caused the failure
     */
    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        emitter.onError(t);
    }

    /**
     * Parses a line from the stream response.
     *
     * @param line the line to parse
     * @return the parsed chunk or null if the line is empty or signifies the end of the stream
     */
    private String parseStreamLine(String line) {
        if (line.isEmpty() || line.equals("data: [DONE]")) {
            return null;  // skip empty lines and the last message
        }
        if (line.startsWith("data: ")) {
            return line.substring(6);  // remove "data: " prefix
        }
        return line;
    }
}
