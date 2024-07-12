package org.haifan.merlin.model.openai;

import okhttp3.ResponseBody;
import org.haifan.merlin.service.StreamHandler;
import retrofit2.Call;
import retrofit2.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class StreamingResponse<T> {
    private final Call<ResponseBody> call;
    private final Function<String, T> chunkParser;
    private final ExecutorService executorService;

    public StreamingResponse(Call<ResponseBody> call, Function<String, T> chunkParser) {
        this.call = call;
        this.chunkParser = chunkParser;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void start(StreamHandler<T> handler) {
        executorService.submit(() -> {
            Response<ResponseBody> response = null;
            try {
                response = call.execute();
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                ResponseBody body = response.body();
                if (body == null) {
                    throw new IOException("Null response body");
                }

                try (BufferedReader reader = new BufferedReader(body.charStream())) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("data: ")) {
                            String json = line.substring(6).trim();
                            if ("[DONE]".equals(json)) {
                                break;
                            }
                            T chunk = chunkParser.apply(json);
                            handler.onNext(chunk);
                        }
                    }
                    handler.onComplete();
                }
            } catch (Exception e) {
                handler.onError(e);
            } finally {
                if (response != null && response.body() != null) {
                    response.body().close();
                }
                executorService.shutdown();
            }
        });
    }

    public void cancel() {
        call.cancel();
        executorService.shutdownNow();
    }
}
