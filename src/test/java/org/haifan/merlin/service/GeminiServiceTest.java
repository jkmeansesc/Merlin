package org.haifan.merlin.service;

import org.haifan.merlin.annotations.UseRelay;
import org.haifan.merlin.annotations.UseWireMock;
import org.haifan.merlin.internal.constants.Provider;
import org.haifan.merlin.internal.utils.DefaultObjectMapper;
import org.haifan.merlin.model.gemini.caching.Content;
import org.haifan.merlin.model.gemini.caching.Part;
import org.haifan.merlin.model.gemini.generatingcontent.GenerateContentRequest;
import org.haifan.merlin.model.gemini.generatingcontent.GenerateContentResponse;
import org.haifan.merlin.model.gemini.models.Model;
import org.haifan.merlin.model.gemini.models.ListModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GeminiServiceTest {
    private LlmConfig config;
    private GeminiService service;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        String baseUrl = GeminiService.DEFAULT_BASE_URL;
        String apiKey = null;
        Properties p = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("test.properties")) {
            p.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load test.properties, supply one if not present.");
        }

        if (testInfo.getTestMethod().isPresent()) {
            var method = testInfo.getTestMethod().get();
            if (method.isAnnotationPresent(UseWireMock.class)) {
                baseUrl = "https://gemini.wiremockapi.cloud/";
            } else if (method.isAnnotationPresent(UseRelay.class)) {
                UseRelay annotation = method.getAnnotation(UseRelay.class);
                baseUrl = annotation.value();
                apiKey = p.getProperty("api.key");
            }
        }

        this.config = new LlmConfig(Provider.GOOGLE_GEMINI, baseUrl, apiKey);
        this.config.setLogLevel(LlmConfig.Level.BODY);
        this.service = Merlin.builder().gemini(config).build().getService(GeminiService.class);
    }

    @Test
    void test_all_constructors_can_be_initialized() {

        LlmService service_1 = new GeminiService();
        assertNotNull(service_1.getClient());
        assertNotNull(service_1.getMapper());
        assertNotNull(service_1.getRetrofit());
        assertNotNull(service_1.getConfig());

        this.config = new LlmConfig(Provider.GOOGLE_GEMINI, "test baseUrl", "test token");
        LlmService service_2 = new GeminiService(config);
        assertEquals("test baseurl", service_2.getConfig().getBaseUrl(), "Wrong baseUrl");
        assertEquals("test token", service_2.getConfig().getToken(), "Wrong token");
    }

    @Nested
    class ModelsTest {
        @Test
        @UseWireMock
        void getModel() {
            String name = "models/gemini-1.5-pro";
            Model response = service.getModel(name).join();
            assertNotNull(response);
            assertEquals(name, response.getName());
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void listModels() {
            service.listModels(10, null).join();
            ListModel response = service.listModels().join();
            response.getModels().forEach(model -> assertNotNull(model.getName()));
            System.out.println(DefaultObjectMapper.print(response));
        }
    }

    @Nested
    class GeneratingContentTest {

        @Test
        void generateContent() {
            String model = "models/gemini-1.5-pro-001";

            List<Part> parts = new ArrayList<>();
            parts.add(Part.builder().build());

            List<Content> contents = new ArrayList<>();
            contents.add(Content.builder().parts(parts).role("user").build());

            GenerateContentRequest request = GenerateContentRequest.builder().contents(contents).build();

            GenerateContentResponse response = service.generateContent(model, request).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        void streamGenerateContent() {
        }
    }

    @Test
    void batchEmbedContents() {
    }

    @Test
    void countTokens() {
    }

    @Test
    void embedContent() {
    }


    @Test
    void listOperations() {
    }

    @Test
    void testListOperations() {
    }

    @Test
    void deleteOperation() {
    }

    @Test
    void cancelTunedModelOperation() {
    }

    @Test
    void getTunedModelOperation() {
    }

    @Test
    void listTunedModelsOperations() {
    }

    @Test
    void testListTunedModelsOperations() {
    }
}