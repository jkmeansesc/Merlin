package org.haifan.merlin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.haifan.merlin.internal.api.OllamaApi;
import org.haifan.merlin.internal.constants.IanaMediaType;
import org.haifan.merlin.internal.constants.Provider;
import org.haifan.merlin.internal.interceptors.OllamaInterceptor;
import org.haifan.merlin.internal.utils.DefaultObjectMapper;
import org.haifan.merlin.model.StreamingResponse;
import org.haifan.merlin.model.ollama.*;
import retrofit2.Call;
import retrofit2.http.Body;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class OllamaService extends LlmService {

    private final OllamaApi api;
    private static final String DEFAULT_BASE_URL = "http://localhost:11434/";

    OllamaService() {
        this(new LlmConfig(Provider.OLLAMA, DEFAULT_BASE_URL, null));
    }

    OllamaService(LlmConfig config) {
        super(config, new OllamaInterceptor());
        this.api = super.retrofit.create(OllamaApi.class);
    }

    private OllamaCompletion parseChunk(String json) {
        ObjectMapper mapper = DefaultObjectMapper.get();
        try {
            return mapper.readValue(json, OllamaCompletion.class);
        } catch (JsonProcessingException e) {
            throw new StreamParsingException("Error parsing JSON chunk", e);
        }
    }

    private OllamaStatus parseStatus(String json) {
        ObjectMapper mapper = DefaultObjectMapper.get();
        try {
            return mapper.readValue(json, OllamaStatus.class);
        } catch (JsonProcessingException e) {
            throw new StreamParsingException("Error parsing JSON chunk", e);
        }
    }

    public StreamingResponse<OllamaCompletion> streamCompletion(OllamaCompletionRequest request) {
        Call<ResponseBody> call = api.streamCompletion(request);
        return new StreamingResponse<>(super.stream(call, this::parseChunk));
    }

    public CompletableFuture<OllamaCompletion> createCompletion(OllamaCompletionRequest request) {
        request.setStream(false);
        return super.call(api.createCompletion(request));
    }

    public CompletableFuture<OllamaCompletion> createChatCompletion(OllamaCompletionRequest request) {
        request.setStream(false);
        return super.call(api.createChatCompletion(request));
    }

    public StreamingResponse<OllamaCompletion> streamChatCompletion(@Body OllamaCompletionRequest request) {
        Call<ResponseBody> call = api.streamChatCompletion(request);
        return new StreamingResponse<>(super.stream(call, this::parseChunk));
    }

    public StreamingResponse<OllamaStatus> createModelStream(OllamaCompletionRequest request) {
        Call<ResponseBody> call = api.createModelStream(request);
        return new StreamingResponse<>(super.stream(call, this::parseStatus));
    }

    public CompletableFuture<OllamaStatus> createModel(OllamaCompletionRequest request) {
        request.setStream(false);
        return super.call(api.createModel(request));
    }

    public CompletableFuture<Void> checkBlob(String digest) {
        return super.call(api.checkBlob(digest));
    }

    public CompletableFuture<Void> createBlob(String digest, File file) {
        RequestBody requestBody = RequestBody.create(file, MediaType.parse(IanaMediaType.OCTET_STREAM));
        return super.call(api.createBlob(digest, requestBody));
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
        return new StreamingResponse<>(super.stream(call, this::parseStatus));
    }

    public CompletableFuture<OllamaStatus> pullModel(OllamaCompletionRequest request) {
        request.setStream(false);
        return super.call(api.pullModel(request));
    }

    public StreamingResponse<OllamaStatus> pushModelStream(OllamaCompletionRequest request) {
        Call<ResponseBody> call = api.pushModelStream(request);
        return new StreamingResponse<>(super.stream(call, this::parseStatus));
    }

    public CompletableFuture<OllamaStatus> pushModel(OllamaCompletionRequest request) {
        request.setStream(false);
        return super.call(api.pushModel(request));
    }

    public CompletableFuture<OllamaEmbedding> createEmbedding(OllamaCompletionRequest request) {
        return super.call(api.createEmbedding(request));
    }

    public CompletableFuture<OllamaModelList> listRunning() {
        return super.call(api.listRunning());
    }
}
