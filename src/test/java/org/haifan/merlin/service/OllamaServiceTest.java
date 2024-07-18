package org.haifan.merlin.service;

import org.haifan.merlin.client.Merlin;
import org.haifan.merlin.model.ollama.OllamaCompletionRequest;
import org.haifan.merlin.model.ollama.OllamaModel;
import org.haifan.merlin.model.ollama.OllamaModelList;
import org.haifan.merlin.utils.JsonPrinter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OllamaServiceTest {

    @Test
    void getConfig() {
    }

    @Test
    void streamCompletion() {
    }

    @Test
    void createCompletion() {
    }

    @Test
    void createChatCompletion() {
    }

    @Test
    void streamChatCompletion() {
    }

    @Test
    void createModelStream() {
    }

    @Test
    void createModel() {
    }

    @Test
    void checkBlob() {
    }

    @Test
    void createBlob() {
    }

    @Test
    void listModels() {
        OllamaModelList list = Merlin.builder()
                .addService(new OllamaService())
                .build()
                .getOllamaService()
                .listModels()
                .join();

        System.out.println(JsonPrinter.print(list));
    }

    @Test
    void showModelInfo() {
        OllamaCompletionRequest request = OllamaCompletionRequest.builder()
                .name("mistral")
                .build();
        OllamaModel model = Merlin.builder()
                .addService(new OllamaService())
                .build()
                .getOllamaService()
                .showModelInfo(request)
                .join();
        System.out.println(JsonPrinter.print(model));
    }

    @Test
    void copyModel() {
    }

    @Test
    void deleteModel() {
    }

    @Test
    void pullModelStream() {
    }

    @Test
    void pullModel() {
    }

    @Test
    void pushModelStream() {
    }

    @Test
    void pushModel() {
    }

    @Test
    void createEmbedding() {
    }

    @Test
    void listRunning() {
        Merlin.builder()
                .addService(new OllamaService())
                .build()
                .getOllamaService()
                .listRunning()
                .join();
    }
}