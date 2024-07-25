package org.haifan.merlin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.haifan.merlin.internal.api.GeminiApi;
import org.haifan.merlin.client.Merlin;
import org.haifan.merlin.internal.config.GeminiConfig;
import org.haifan.merlin.internal.interceptors.GeminiInterceptor;
import org.haifan.merlin.model.gemini.ModelList;
import org.haifan.merlin.internal.utils.DefaultObjectMapper;
import org.haifan.merlin.utils.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import retrofit2.Call;
import retrofit2.Retrofit;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GeminiServiceTest {
    @Mock
    private GeminiApi api;
    private final GeminiConfig config = new GeminiConfig();
    private final GeminiInterceptor interceptor = new GeminiInterceptor("");
    private GeminiService service;
    private final ObjectMapper mapper = DefaultObjectMapper.create();

    @BeforeEach
    void setUp() {
        if (config.useMock()) {
            service = new GeminiService(api, config, interceptor);
        } else {
            service = new GeminiService();
        }
    }

    @Test
    void test_all_constructors_can_be_initialized() {

        List<Retrofit> retrofitList = new ArrayList<>();

        LlmService service_1 = new GeminiService();
        JsonNode config_1 = service_1.getConfig();
        retrofitList.add(service_1.getRetrofit());
        assertNotNull(config_1, "Config should not be null");
        assertTrue(config_1.has("baseUrl"), "Config should contain the key 'baseUrl'");

        LlmService service_2 = new GeminiService("foo");
        JsonNode config_2 = service_2.getConfig();
        retrofitList.add(service_2.getRetrofit());
        assertNotNull(config_2, "Config should not be null");
        assertTrue(config_2.has("baseUrl"), "Config should contain the key 'baseUrl'");

        LlmService service_3 = new GeminiService("src/test/resources/test_config.json", true);
        JsonNode config_3 = service_3.getConfig();
        retrofitList.add(service_3.getRetrofit());
        assertNotNull(config_3, "Config should not be null");
        assertTrue(config_3.has("baseUrl"), "Config should contain the key 'baseUrl'");
        assertEquals("https://do.not.delete.this.or.test.will.fail.com", config_3.get("baseUrl").asText(), "Wrong config is loaded");

        LlmService service_4 = new GeminiService("foo", "src/test/resources/test_config.json");
        JsonNode config_4 = service_4.getConfig();
        retrofitList.add(service_4.getRetrofit());
        assertNotNull(config_4, "Config should not be null");
        assertTrue(config_4.has("baseUrl"), "Config should contain the key 'baseUrl'");
        assertEquals("https://do.not.delete.this.or.test.will.fail.com", config_4.get("baseUrl").asText(), "Wrong config is loaded");

        retrofitList.forEach(retrofit -> assertNotNull(retrofit, "Retrofit should not be null"));
    }

    @Nested
    class V1Test {
        @Test
        void listModels() {
            if (config.useMock()) {
                String expected = TestHelper.read("google_gemini/gemini_model_list.json");
                Call<ModelList> call = mock(Call.class);
                when(api.listModels()).thenReturn(call);
                TestHelper.setupSuccessfulAsyncResponseWithJson(call, expected, ModelList.class, mapper);
            }

            ModelList response = Merlin.builder()
                    .addService(service)
                    .build()
                    .getGeminiService()
                    .listModels()
                    .join();
            response.getModels().forEach(model -> assertNotNull(model.getName()));
        }
    }
}