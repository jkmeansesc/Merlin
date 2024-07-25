package org.haifan.merlin.client;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.haifan.merlin.service.GeminiService;
import org.haifan.merlin.service.LlmService;
import org.haifan.merlin.service.OllamaService;
import org.haifan.merlin.service.OpenAiService;

import java.util.HashMap;
import java.util.Map;

@Builder
@SuppressWarnings("unused")
public class Merlin {

    @Getter(AccessLevel.PACKAGE)
    private final Map<Class<? extends LlmService>, LlmService> services;

    public OpenAiService getOpenAiService() {
        return getService(OpenAiService.class);
    }

    public GeminiService getGeminiService() {
        return getService(GeminiService.class);
    }

    public OllamaService getOllamaService() {
        return getService(OllamaService.class);
    }

    // O(1) operation
    @SuppressWarnings("unchecked")
    private <T extends LlmService> T getService(Class<T> serviceClass) {
        LlmService service = services.get(serviceClass);
        if (service == null) {
            throw new IllegalStateException(serviceClass.getSimpleName() + " not found");
        }
        return (T) service;
    }

    public static class MerlinBuilder {
        private Map<Class<? extends LlmService>, LlmService> services;

        public MerlinBuilder addService(LlmService service) {
            this.services.put(service.getClass(), service);
            return this;
        }

        public MerlinBuilder openai() {
            this.services.put(OpenAiService.class, new OpenAiService());
            return this;
        }

        public MerlinBuilder openai(String token) {
            this.services.put(OpenAiService.class, new OpenAiService(token));
            return this;
        }

        public MerlinBuilder gemini() {
            this.services.put(GeminiService.class, new GeminiService());
            return this;
        }

        public MerlinBuilder gemini(String token) {
            this.services.put(GeminiService.class, new GeminiService(token));
            return this;
        }

        public MerlinBuilder ollama() {
            this.services.put(OllamaService.class, new OllamaService());
            return this;
        }

        public Merlin build() {
            return new Merlin(new HashMap<>(services));
        }
    }
}
