package org.haifan.merlin.service;

import org.haifan.merlin.api.OpenAiApi;
import org.haifan.merlin.model.openai.OpenAiResponse;
import org.haifan.merlin.model.openai.models.Model;

import java.util.concurrent.CompletableFuture;

public class OpenAiService extends LlmService {

    private final OpenAiApi api;

    public OpenAiService(String baseUrl, String apiKey) {
        super(baseUrl, apiKey);
        this.api = super.retrofit.create(OpenAiApi.class);
    }

    public CompletableFuture<OpenAiResponse<Model>> listModels() {
        return super.executeCall(api.listModels());
    }

    public CompletableFuture<Model> getModel(String model) {
        return super.executeCall(api.getModel(model));
    }
}
