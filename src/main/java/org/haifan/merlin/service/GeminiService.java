package org.haifan.merlin.service;

import okhttp3.ResponseBody;
import org.haifan.merlin.api.GeminiApi;
import org.haifan.merlin.config.GeminiConfig;
import org.haifan.merlin.config.LlmConfig;
import org.haifan.merlin.interceptors.GeminiInterceptor;
import org.haifan.merlin.model.gemini.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GeminiService extends LlmService {

    private final GeminiApi api;

    public GeminiService() {
        this(new GeminiConfig());
    }

    public GeminiService(String apiKey) {
        this(new GeminiConfig(apiKey));
    }

    public GeminiService(String configPath, boolean isConfigPath) {
        this(new GeminiConfig(configPath, isConfigPath));
    }

    public GeminiService(String apiKey, String configPath) {
        this(new GeminiConfig(apiKey, configPath));
    }

    private GeminiService(GeminiConfig config) {
        super(config, new GeminiInterceptor(config.getApiKey()));
        this.api = super.retrofit.create(GeminiApi.class);
    }


    // ===============================
    // Google Gemini v1
    // ===============================

    public CompletableFuture<BatchEmbedContentsResponse> batchEmbedContents(String model, BatchEmbedContentsRequest request) {
        return super.call(api.batchEmbedContents(model, request));
    }

    public CompletableFuture<CountTokensResponse> countTokens(String model, CountTokensRequest request) {
        return super.call(api.countTokens(model, request));
    }

    public CompletableFuture<EmbedContentResponse> embedContent(String model, EmbedContentRequest request) {
        return super.call(api.embedContent(model, request));
    }

    public CompletableFuture<GenerateContentResponse> generateContent(String model, GenerateContentRequest request) {
        return super.call(api.generateContent(model, request));
    }

    public CompletableFuture<Model> getModel(String name) {
        return super.call(api.getModel(name));
    }

    public CompletableFuture<ModelList> listModels() {
        return super.call(api.listModels());
    }

    public CompletableFuture<ModelList> listModels(Integer pageSize, String pageToken) {
        return super.call(api.listModels(pageSize, pageToken));
    }

    // TODO: support streaming
    public CompletableFuture<ResponseBody> streamGenerateContent(String model, GenerateContentRequest request) {
        return super.call(api.streamGenerateContent(model, request));
    }

    public CompletableFuture<ListOperationsResponse> listOperations(String name) {
        return super.call(api.listOperations(name));
    }

    public CompletableFuture<ListOperationsResponse> listOperations(String name, Map<String, Object> queryMap) {
        return super.call(api.listOperations(name, queryMap));
    }

    public CompletableFuture<Void> deleteOperation(String name) {
        return super.call(api.deleteOperation(name));
    }

    public CompletableFuture<Void> cancelTunedModelOperation(String name) {
        return super.call(api.cancelTunedModelOperation(name));
    }

    public CompletableFuture<Operation> getTunedModelOperation(String name) {
        return super.call(api.getTunedModelOperation(name));
    }

    public CompletableFuture<ListOperationsResponse> listTunedModelsOperations(String name) {
        return super.call(api.listTunedModelsOperations(name));
    }

    public CompletableFuture<ListOperationsResponse> listTunedModelsOperations(String name, Map<String, Object> queryMap) {
        return super.call(api.listTunedModelsOperations(name, queryMap));
    }
}
