package org.merlin.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MerlinTest {

    @Test
    void testDefaultServices() {
        Merlin merlin = Merlin
                .builder()
                .openai()
                .gemini()
                .ollama()
                .build();
        OpenAiService openAiService = merlin.getService(OpenAiService.class);
        GeminiService geminiService = merlin.getService(GeminiService.class);
        OllamaService ollamaService = merlin.getService(OllamaService.class);

        assertNotNull(openAiService);
        assertNotNull(geminiService);
        assertNotNull(ollamaService);
    }

    @Test
    void testServicesWithToken() {
        Merlin merlin = Merlin
                .builder()
                .openai("some openai token")
                .gemini("some gemini token")
                .build();

        OpenAiService openAiService = merlin.getService(OpenAiService.class);
        assertNotNull(openAiService);
        GeminiService geminiService = merlin.getService(GeminiService.class);
        assertNotNull(geminiService);
    }
}