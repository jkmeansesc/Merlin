package org.haifan.merlin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.haifan.merlin.annotations.UseRelay;
import org.haifan.merlin.annotations.UseWireMock;
import org.haifan.merlin.client.Merlin;
import org.haifan.merlin.internal.constants.Provider;
import org.haifan.merlin.internal.interceptors.GeminiInterceptor;
import org.haifan.merlin.model.gemini.ModelList;
import org.haifan.merlin.internal.utils.DefaultObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

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

        this.config = new LlmConfig(Provider.OPENAI, baseUrl, apiKey);
        this.config.setLogLevel(LlmConfig.Level.BODY);
        this.service = new GeminiService();
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
    class V1Test {
        @Test
        void listModels() {

            ModelList response = Merlin.builder()
                    .addService(service)
                    .build()
                    .getService(GeminiService.class)
                    .listModels()
                    .join();
            response.getModels().forEach(model -> assertNotNull(model.getName()));
        }
    }
}