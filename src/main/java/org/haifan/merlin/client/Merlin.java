package org.haifan.merlin.client;

import lombok.Builder;
import lombok.Getter;
import org.haifan.merlin.service.GeminiService;
import org.haifan.merlin.service.LlmService;
import org.haifan.merlin.service.OllamaService;
import org.haifan.merlin.service.OpenAiService;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: add javadoc
 */
@Getter
@Builder
public class Merlin {

    private List<LlmService> services;

    public OpenAiService getOpenAiService() {
        return (OpenAiService) services.stream()
                .filter(OpenAiService.class::isInstance)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("OpenAiService not found"));
    }

    public GeminiService getGeminiService() {
        return (GeminiService) services.stream()
                .filter(GeminiService.class::isInstance)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("GeminiService not found"));
    }

    public OllamaService getOllamaService() {
        return (OllamaService) services.stream()
                .filter(OllamaService.class::isInstance)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("OllamaService not found"));
    }

    @SuppressWarnings("unused")
    public static class MerlinBuilder {
        public MerlinBuilder addService(LlmService service) {
            if (services == null) {
                services = new ArrayList<>();
            }
            this.services.add(service);
            return this;
        }
    }
}
