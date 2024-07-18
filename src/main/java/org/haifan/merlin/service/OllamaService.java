package org.haifan.merlin.service;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.haifan.merlin.api.OllamaApi;
import org.haifan.merlin.config.LlmConfig;
import org.haifan.merlin.config.OllamaConfig;
import org.haifan.merlin.interceptors.OllamaInterceptor;
import org.haifan.merlin.model.ollama.*;
import org.haifan.merlin.model.openai.StreamingResponse;
import org.haifan.merlin.utils.FileParser;
import retrofit2.Call;
import retrofit2.http.Body;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class OllamaService extends LlmService {

    private final OllamaApi api;

    public OllamaService() {
        this(new OllamaConfig());
    }

    public OllamaService(String configPath) {
        this(new OllamaConfig(configPath));
    }

    private OllamaService(OllamaConfig config) {
        super(config, new OllamaInterceptor());
        this.api = super.retrofit.create(OllamaApi.class);
    }

    // TODO: implement this
    private OllamaCompletion parseChunk(String json) {
        // Use Jackson or Gson to parse JSON into ChatCompletionChunk
        return null;
    }

    // TODO: implement this
    private OllamaStatus parseStatus(String json) {
        return null;
    }

    public StreamingResponse<OllamaCompletion> streamCompletion(OllamaCompletionRequest request) {
        Call<ResponseBody> call = api.streamCompletion(request);
        return new StreamingResponse<>(call, this::parseChunk);
    }

    public CompletableFuture<OllamaCompletion> createCompletion(OllamaCompletionRequest request) {
        return super.call(api.createCompletion(request));
    }

    public CompletableFuture<OllamaCompletion> createChatCompletion(OllamaCompletionRequest request) {
        return super.call(api.createChatCompletion(request));
    }

    public StreamingResponse<OllamaCompletion> streamChatCompletion(@Body OllamaCompletionRequest request) {
        Call<ResponseBody> call = api.streamChatCompletion(request);
        return new StreamingResponse<>(call, this::parseChunk);
    }

    public StreamingResponse<OllamaStatus> createModelStream(OllamaCompletionRequest request) {
        Call<ResponseBody> call = api.createModelStream(request);
        return new StreamingResponse<>(call, this::parseStatus);
    }

    public CompletableFuture<OllamaStatus> createModel(OllamaCompletionRequest request) {
        return super.call(api.createModel(request));
    }

    public CompletableFuture<Void> checkBlob(String digest) {
        return super.call(api.checkBlob(digest));
    }

    public CompletableFuture<Void> createBlob(String digest, File file) {
        RequestBody requestBody = FileParser.parseFile(file);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(requestBody);
        return super.call(api.createBlob(digest, builder.build()));
    }

    public CompletableFuture<OllamaModelList> listModels() {
        return super.call(api.listModels());
    }


    public CompletableFuture<OllamaModel> showModelInfo(OllamaCompletionRequest request) {
        return super.call(api.showModelInfo(request));
    }

    public CompletableFuture<Void> copyModel(OllamaCompletionRequest request) {
        return super.call(api.copyModel(request));
    }

    public CompletableFuture<Void> deleteModel(OllamaCompletionRequest request) {
        return super.call(api.deleteModel(request));
    }

    public StreamingResponse<OllamaStatus> pullModelStream(OllamaCompletionRequest request) {
        Call<ResponseBody> call = api.pullModelStream(request);
        return new StreamingResponse<>(call, this::parseStatus);
    }

    public CompletableFuture<OllamaStatus> pullModel(OllamaCompletionRequest request) {
        return super.call(api.pullModel(request));
    }

    public StreamingResponse<OllamaStatus> pushModelStream(OllamaCompletionRequest request) {
        Call<ResponseBody> call = api.pushModelStream(request);
        return new StreamingResponse<>(call, this::parseStatus);
    }

    public CompletableFuture<OllamaStatus> pushModel(OllamaCompletionRequest request) {
        return super.call(api.pushModel(request));
    }

    public CompletableFuture<OllamaEmbedding> createEmbedding(OllamaCompletionRequest request) {
        return super.call(api.createEmbedding(request));
    }

    public CompletableFuture<OllamaModel> listRunning() {
        return super.call(api.listRunning());
    }
}
