package org.haifan.merlin.service;

import okhttp3.ResponseBody;
import org.haifan.merlin.internal.api.GeminiApi;
import org.haifan.merlin.internal.constants.Provider;
import org.haifan.merlin.internal.interceptors.GeminiInterceptor;
import org.haifan.merlin.model.gemini.*;
import org.haifan.merlin.model.gemini.embeddings.EmbedContentRequest;
import org.haifan.merlin.model.gemini.generatingcontent.GenerateContentRequest;
import org.haifan.merlin.model.gemini.generatingcontent.GenerateContentResponse;
import org.haifan.merlin.model.gemini.models.Model;
import org.haifan.merlin.model.gemini.models.ListModel;
import org.haifan.merlin.model.gemini.tokens.CountTokensRequest;
import org.haifan.merlin.model.gemini.tokens.CountTokensResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GeminiService extends LlmService {

    private final GeminiApi api;
    public static final String DEFAULT_BASE_URL = "https://generativelanguage.googleapis.com";

    GeminiService() {
        this(new LlmConfig(Provider.GOOGLE_GEMINI, DEFAULT_BASE_URL, null));
    }

    GeminiService(LlmConfig config) {
        super(config, new GeminiInterceptor(config.getToken()));
        this.api = super.retrofit.create(GeminiApi.class);
    }

    // ===============================
    // Google Gemini v1
    // ===============================

    // ===============================
    // Models
    // ===============================

    public CompletableFuture<Model> getModel(String name) {
        return super.call(api.getModel(name));
    }

    public CompletableFuture<ListModel> listModels() {
        return super.call(api.listModels());
    }

    public CompletableFuture<ListModel> listModels(Integer pageSize, String pageToken) {
        return super.call(api.listModels(pageSize, pageToken));
    }

    // ===============================
    // Generating Content
    // ===============================

    public CompletableFuture<GenerateContentResponse> generateContent(String model, GenerateContentRequest request) {
        return super.call(api.generateContent(model, request));
    }

    // TODO: support streaming
    public CompletableFuture<ResponseBody> streamGenerateContent(String model, GenerateContentRequest request) {
        return super.call(api.streamGenerateContent(model, request));
    }

    public CompletableFuture<BatchEmbedContentsResponse> batchEmbedContents(String model, BatchEmbedContentsRequest request) {
        return super.call(api.batchEmbedContents(model, request));
    }

    public CompletableFuture<CountTokensResponse> countTokens(String model, CountTokensRequest request) {
        return super.call(api.countTokens(model, request));
    }

    public CompletableFuture<EmbedContentResponse> embedContent(String model, EmbedContentRequest request) {
        return super.call(api.embedContent(model, request));
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
