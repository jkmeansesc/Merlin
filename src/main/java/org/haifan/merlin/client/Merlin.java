package org.haifan.merlin.client;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.haifan.merlin.internal.constants.Provider;
import org.haifan.merlin.service.*;

import java.util.HashMap;
import java.util.Map;

@Builder
@SuppressWarnings("unused")
public class Merlin {

    @Getter(AccessLevel.PACKAGE)
    private final Map<Class<? extends LlmService>, LlmService> services;

    // O(1) operation
    public <T extends LlmService> T getService(Class<T> serviceClass) {
        return serviceClass.cast(services.get(serviceClass));
    }

    public static class MerlinBuilder {

        private MerlinBuilder() {
            this.services = new HashMap<>();
        }

        public MerlinBuilder addService(LlmService service) {
            this.services.put(service.getClass(), service);
            return this;
        }

        public MerlinBuilder openai() {
            this.services.put(OpenAiService.class, new OpenAiService());
            return this;
        }

        public MerlinBuilder openai(String token) {
            LlmConfig config = new LlmConfig(Provider.OPENAI, OpenAiService.DEFAULT_BASE_URL, token);
            this.services.put(OpenAiService.class, new OpenAiService(config));
            return this;
        }

        public MerlinBuilder openaiWith(String baseUrl) {
            LlmConfig config = new LlmConfig(Provider.OPENAI, baseUrl, null);
            this.services.put(OpenAiService.class, new OpenAiService(config));
            return this;
        }

        public MerlinBuilder openaiWith(String baseUrl, String token) {
            LlmConfig config = new LlmConfig(Provider.OPENAI, baseUrl, token);
            this.services.put(OpenAiService.class, new OpenAiService(config));
            return this;
        }

        public MerlinBuilder gemini() {
            this.services.put(GeminiService.class, new GeminiService());
            return this;
        }

        public MerlinBuilder gemini(String token) {
            LlmConfig config = new LlmConfig(Provider.GOOGLE_GEMINI, GeminiService.DEFAULT_BASE_URL, token);
            this.services.put(GeminiService.class, new GeminiService(config));
            return this;
        }

        public MerlinBuilder geminiWith(String baseUrl) {
            LlmConfig config = new LlmConfig(Provider.GOOGLE_GEMINI, baseUrl, null);
            this.services.put(GeminiService.class, new GeminiService(config));
            return this;
        }

        public MerlinBuilder geminiWith(String baseUrl, String token) {
            LlmConfig config = new LlmConfig(Provider.GOOGLE_GEMINI, baseUrl, token);
            this.services.put(GeminiService.class, new GeminiService(config));
            return this;
        }

        public MerlinBuilder ollama() {
            this.services.put(OllamaService.class, new OllamaService());
            return this;
        }

        public MerlinBuilder ollama(String baseUrl) {
            LlmConfig config = new LlmConfig(Provider.OLLAMA, baseUrl, null);
            this.services.put(OllamaService.class, new OllamaService(config));
            return this;
        }

        public Merlin build() {
            return new Merlin(new HashMap<>(services));
        }
    }
}
