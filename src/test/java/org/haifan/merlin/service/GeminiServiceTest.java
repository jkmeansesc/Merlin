package org.haifan.merlin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.haifan.merlin.client.Merlin;
import org.haifan.merlin.internal.constants.Provider;
import org.haifan.merlin.internal.interceptors.GeminiInterceptor;
import org.haifan.merlin.model.gemini.ModelList;
import org.haifan.merlin.internal.utils.DefaultObjectMapper;
import org.haifan.merlin.utils.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeminiServiceTest {
    private LlmConfig config;
    private final GeminiInterceptor interceptor = new GeminiInterceptor("");
    private GeminiService service;
    private final ObjectMapper mapper = DefaultObjectMapper.create();

    @BeforeEach
    void setUp() {
        if (TestConfig.useMock()) {
            this.config = new LlmConfig(Provider.GOOGLE_GEMINI, "https://gemini.wiremockapi.cloud/", null);
        } else {
            this.config = new LlmConfig(Provider.GOOGLE_GEMINI, GeminiService.DEFAULT_BASE_URL, null);
        }
        this.config.setLogLevel(LlmConfig.Level.BODY);
        service = new GeminiService();
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