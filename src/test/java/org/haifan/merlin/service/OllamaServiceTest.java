package org.haifan.merlin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.haifan.merlin.api.OllamaApi;
import org.haifan.merlin.client.Merlin;
import org.haifan.merlin.config.OllamaConfig;
import org.haifan.merlin.interceptors.OllamaInterceptor;
import org.haifan.merlin.model.ollama.OllamaCompletionRequest;
import org.haifan.merlin.model.ollama.OllamaEmbedding;
import org.haifan.merlin.model.ollama.OllamaModel;
import org.haifan.merlin.model.ollama.OllamaModelList;
import org.haifan.merlin.utils.DefaultObjectMapper;
import org.haifan.merlin.utils.JsonPrinter;
import org.haifan.merlin.utils.TestHelper;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OllamaServiceTest {

    @Mock
    private OllamaApi api;
    private final OllamaConfig config = new OllamaConfig();
    private final OllamaInterceptor interceptor = new OllamaInterceptor();
    private OllamaService service;
    private final ObjectMapper mapper = DefaultObjectMapper.create();

    @BeforeEach
    void setUp() {
        if (config.useMock()) {
            service = new OllamaService(api, config, interceptor);
        } else {
            service = new OllamaService();
        }
    }

    @Test
    void test_all_constructors_can_be_initialized() {
        List<Retrofit> retrofitList = new ArrayList<>();

        LlmService service_1 = new OllamaService();
        retrofitList.add(service_1.getRetrofit());
        JsonNode config_1 = service_1.getConfig();
        assertNotNull(config_1, "Config should not be null");
        assertTrue(config_1.has("baseUrl"), "Config should contain the key 'baseUrl'");

        LlmService service_2 = new OllamaService("src/test/resources/test_config.json");
        retrofitList.add(service_2.getRetrofit());
        JsonNode config_2 = service_2.getConfig();
        assertNotNull(config_2, "Config should not be null");
        assertTrue(config_2.has("baseUrl"), "Config should contain the key 'baseUrl'");
        assertEquals("https://do.not.delete.this.or.test.will.fail.com", config_2.get("baseUrl").asText(), "Wrong config is loaded");

        retrofitList.forEach(retrofit -> assertNotNull(retrofit, "Retrofit should not be null"));
    }

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
        if (config.useMock()) {
            String expected = TestHelper.read("ollama/ollama_model_list.json");
            Call<OllamaModelList> call = mock(Call.class);
            when(api.listModels()).thenReturn(call);
            TestHelper.setupSuccessfulAsyncResponseWithJson(call, expected, OllamaModelList.class, mapper);
        }

        OllamaModelList response = Merlin.builder()
                .addService(service)
                .build()
                .getOllamaService()
                .listModels()
                .join();
        assertNotNull(response.getModels());
        assertEquals(1, response.getModels().size());
        assertEquals("mistral:latest", response.getModels().get(0).getModel());
        System.out.println(JsonPrinter.print(response));
    }

    @Test
    void showModelInfo() {
        if (config.useMock()) {
            String expected = TestHelper.read("ollama/ollama_model_info.json");
            Call<OllamaModel> call = mock(Call.class);
            when(api.showModelInfo(any(OllamaCompletionRequest.class))).thenReturn(call);
            TestHelper.setupSuccessfulAsyncResponseWithJson(call, expected, OllamaModel.class, mapper);
        }

        OllamaCompletionRequest request = OllamaCompletionRequest.builder()
                .name("mistral")
                .build();
        OllamaModel response = Merlin.builder()
                .addService(service)
                .build()
                .getOllamaService()
                .showModelInfo(request)
                .join();
        assertNotNull(response.getModelInfo());
        System.out.println(JsonPrinter.print(response));
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
        if (config.useMock()) {
            String expected = TestHelper.read("ollama/ollama_embedding.json");
            Call<OllamaEmbedding> call = mock(Call.class);
            when(api.createEmbedding(any(OllamaCompletionRequest.class))).thenReturn(call);
            TestHelper.setupSuccessfulAsyncResponseWithJson(call, expected, OllamaEmbedding.class, mapper);
        }

        OllamaCompletionRequest request = OllamaCompletionRequest.builder()
                .model("mistral")
                .prompt("Here is an article about llamas...")
                .build();
        OllamaEmbedding response = Merlin.builder()
                .addService(service)
                .build()
                .getOllamaService()
                .createEmbedding(request)
                .join();
        assertNotNull(response.getEmbedding());
        System.out.println(JsonPrinter.print(response));
    }


    @Test
    void listRunning() {
        if (config.useMock()) {
            String expected = TestHelper.read("ollama/ollama_model_list.json");
            Call<OllamaModelList> call = mock(Call.class);
            when(api.listRunning()).thenReturn(call);
            TestHelper.setupSuccessfulAsyncResponseWithJson(call, expected, OllamaModelList.class, mapper);
        }

        OllamaModelList response = Merlin.builder()
                .addService(service)
                .build()
                .getOllamaService()
                .listRunning()
                .join();
        assertNotNull(response.getModels());
        System.out.println(JsonPrinter.print(response));
    }
}