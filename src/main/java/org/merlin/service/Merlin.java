package org.merlin.service;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.merlin.internal.constants.Provider;

import java.util.HashMap;
import java.util.Map;

/**
 * Main class for managing Large Language Model (LLM) services.
 * This class uses a builder pattern to configure and provide access to various LLM services.
 */
@Builder
@SuppressWarnings("unused")
public class Merlin {

    @Getter(AccessLevel.PACKAGE)
    private final Map<Class<? extends LlmService>, LlmService> services;

    /**
     * Retrieves an instance of the specified LLM service.
     *
     * @param <T>          the type of LLM service.
     * @param serviceClass the class of the LLM service to retrieve.
     * @return an instance of the specified LLM service.
     */
    public <T extends LlmService> T getService(Class<T> serviceClass) {
        return serviceClass.cast(services.get(serviceClass));
    }

    public static class MerlinBuilder {

        private MerlinBuilder() {
            this.services = new HashMap<>();
        }

        /**
         * Configures the builder to use the OpenAI service with default settings.
         *
         * @return the current instance of the builder.
         */
        public MerlinBuilder openai() {
            this.services.put(OpenAiService.class, new OpenAiService());
            return this;
        }

        /**
         * Configures the builder to use the OpenAI service with a specified token.
         *
         * @param token the API token for the OpenAI service.
         * @return the current instance of the builder.
         */
        public MerlinBuilder openai(String token) {
            LlmConfig config = new LlmConfig(Provider.OPENAI, OpenAiService.DEFAULT_BASE_URL, token);
            this.services.put(OpenAiService.class, new OpenAiService(config));
            return this;
        }

        /**
         * Configures the builder to use the OpenAI service with a specified token.
         *
         * @param config for the OpenAI service.
         * @return the current instance of the builder.
         */
        public MerlinBuilder openai(LlmConfig config) {
            this.services.put(OpenAiService.class, new OpenAiService(config));
            return this;
        }

        /**
         * Configures the builder to use the OpenAI service with a specified base URL.
         *
         * @param baseUrl the base URL for the OpenAI service.
         * @return the current instance of the builder.
         */
        public MerlinBuilder openaiWith(String baseUrl) {
            LlmConfig config = new LlmConfig(Provider.OPENAI, baseUrl, null);
            this.services.put(OpenAiService.class, new OpenAiService(config));
            return this;
        }

        /**
         * Configures the builder to use the OpenAI service with a specified base URL and token.
         *
         * @param baseUrl the base URL for the OpenAI service.
         * @param token   the API token for the OpenAI service.
         * @return the current instance of the builder.
         */
        public MerlinBuilder openaiWith(String baseUrl, String token) {
            LlmConfig config = new LlmConfig(Provider.OPENAI, baseUrl, token);
            this.services.put(OpenAiService.class, new OpenAiService(config));
            return this;
        }

        /**
         * Configures the builder to use the Google Gemini service with default settings.
         *
         * @return the current instance of the builder.
         */
        public MerlinBuilder gemini() {
            this.services.put(GeminiService.class, new GeminiService());
            return this;
        }

        /**
         * Configures the builder to use the Google Gemini service with a specified token.
         *
         * @param token the API token for the Google Gemini service.
         * @return the current instance of the builder.
         */
        public MerlinBuilder gemini(String token) {
            LlmConfig config = new LlmConfig(Provider.GOOGLE_GEMINI, GeminiService.DEFAULT_BASE_URL, token);
            this.services.put(GeminiService.class, new GeminiService(config));
            return this;
        }

        /**
         * Configures the builder to use the Google Gemini service with a specified config.
         *
         * @param config for the Google Gemini service.
         * @return the current instance of the builder.
         */
        public MerlinBuilder gemini(LlmConfig config) {
            this.services.put(GeminiService.class, new GeminiService(config));
            return this;
        }

        /**
         * Configures the builder to use the Google Gemini service with a specified base URL.
         *
         * @param baseUrl the base URL for the Google Gemini service.
         * @return the current instance of the builder.
         */
        public MerlinBuilder geminiWith(String baseUrl) {
            LlmConfig config = new LlmConfig(Provider.GOOGLE_GEMINI, baseUrl, null);
            this.services.put(GeminiService.class, new GeminiService(config));
            return this;
        }

        /**
         * Configures the builder to use the Google Gemini service with a specified base URL and token.
         *
         * @param baseUrl the base URL for the Google Gemini service.
         * @param token   the API token for the Google Gemini service.
         * @return the current instance of the builder.
         */
        public MerlinBuilder geminiWith(String baseUrl, String token) {
            LlmConfig config = new LlmConfig(Provider.GOOGLE_GEMINI, baseUrl, token);
            this.services.put(GeminiService.class, new GeminiService(config));
            return this;
        }


        /**
         * Configures the builder to use the Ollama service with default settings.
         *
         * @return the current instance of the builder.
         */
        public MerlinBuilder ollama() {
            this.services.put(OllamaService.class, new OllamaService());
            return this;
        }

        /**
         * Configures the builder to use the Ollama service with a specified base URL.
         *
         * @param baseUrl the base URL for the Ollama service.
         * @return the current instance of the builder.
         */
        public MerlinBuilder ollama(String baseUrl) {
            LlmConfig config = new LlmConfig(Provider.OLLAMA, baseUrl, null);
            this.services.put(OllamaService.class, new OllamaService(config));
            return this;
        }

        /**
         * Configures the builder to use the Ollama service with a specified config.
         *
         * @param config for the Ollama service.
         * @return the current instance of the builder.
         */
        public MerlinBuilder ollama(LlmConfig config) {
            this.services.put(OllamaService.class, new OllamaService(config));
            return this;
        }

        /**
         * Builds and returns an instance of {@link Merlin} with the configured services.
         *
         * @return an instance of {@link Merlin}.
         */
        public Merlin build() {
            return new Merlin(new HashMap<>(services));
        }
    }
}
