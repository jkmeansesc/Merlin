package org.haifan.merlin.service;

import org.haifan.merlin.api.OpenAiApi;
import org.haifan.merlin.config.LlmConfig;
import org.haifan.merlin.config.OpenAiConfig;
import org.haifan.merlin.model.openai.OpenAiResponse;
import org.haifan.merlin.model.openai.models.Model;

import java.util.concurrent.CompletableFuture;

/**
 * TODO: add javadoc
 */
public class OpenAiService extends LlmService {

    private final OpenAiApi api;

    public OpenAiService() {
        super(new OpenAiConfig());
        this.api = super.retrofit.create(OpenAiApi.class);
    }

    public OpenAiService(String apiKey) {
        super(new OpenAiConfig(apiKey));
        this.api = super.retrofit.create(OpenAiApi.class);
    }

    @Override
    public LlmConfig getConfig() {
        return super.llmConfig;
    }

    public CompletableFuture<OpenAiResponse<Model>> listModels() {
        return super.executeCall(api.listModels());
    }

    public CompletableFuture<Model> getModel(String model) {
        return super.executeCall(api.getModel(model));
    }
}
