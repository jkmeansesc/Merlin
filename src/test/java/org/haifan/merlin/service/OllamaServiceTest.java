package org.haifan.merlin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.RequestBody;
import org.haifan.merlin.api.OllamaApi;
import org.haifan.merlin.client.Merlin;
import org.haifan.merlin.config.OllamaConfig;
import org.haifan.merlin.interceptors.OllamaInterceptor;
import org.haifan.merlin.model.ollama.*;
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

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

        assertNotNull(service_1.getObjectMapper());
        assertNotNull(service_1.getOkHttpClient());

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
    void streamCompletion() {
    }

    @Test
    void createCompletion() {

        if (config.useMock()) {
            String expected = TestHelper.read("ollama/ollama_completion_no_stream.json");
            Call<OllamaCompletion> call = mock(Call.class);
            when(api.createCompletion(any(OllamaCompletionRequest.class))).thenReturn(call);
            TestHelper.setupSuccessfulAsyncResponseWithJson(call, expected, OllamaCompletion.class, mapper);
        }

        OllamaCompletionRequest request = OllamaCompletionRequest
                .builder()
                .model("mistral")
                .prompt("Why is the sky blue?")
                .stream(false)
                .build();

        OllamaCompletion response = Merlin.builder()
                .addService(service)
                .build()
                .getOllamaService()
                .createCompletion(request)
                .join();

        assertTrue(response.getDone(), "Non streaming response should always return true");
        assertEquals("stop", response.getDoneReason(), "Non streaming response should always return stop");
        assertNotNull(response.getResponse(), "Response should not be null");
        System.out.println(JsonPrinter.print(response));
    }

    @Test
    void createChatCompletion() {

        if (config.useMock()) {
            String expected = TestHelper.read("ollama/ollama_chat_completion_no_stream.json");
            Call<OllamaCompletion> call = mock(Call.class);
            when(api.createChatCompletion(any(OllamaCompletionRequest.class))).thenReturn(call);
            TestHelper.setupSuccessfulAsyncResponseWithJson(call, expected, OllamaCompletion.class, mapper);
        }

        List<OllamaMessage> messages = new ArrayList<>();
        OllamaMessage message = new OllamaMessage();
        message.setRole("user");
        message.setContent("Are you online?");
        messages.add(message);

        OllamaCompletionRequest request = OllamaCompletionRequest
                .builder()
                .model("mistral")
                .messages(messages)
                .stream(false)
                .build();

        OllamaCompletion response = Merlin.builder()
                .addService(service)
                .build()
                .getOllamaService()
                .createChatCompletion(request)
                .join();

        assertTrue(response.getDone(), "Non streaming response should always return true");
        assertEquals("stop", response.getDoneReason(), "Non streaming response should always return stop");
        assertNotNull(response.getMessage(), "Response should not be null");
        System.out.println(JsonPrinter.print(response));
    }

    @Test
    void streamChatCompletion() {
    }

    @Test
    void createModelStream() {
    }

    @Test
    void createModel() {
        if (config.useMock()) {
            String expected = TestHelper.read("ollama/ollama_status_success.json");
            Call<OllamaStatus> call = mock(Call.class);
            when(api.createModel(any(OllamaCompletionRequest.class))).thenReturn(call);
            TestHelper.setupSuccessfulAsyncResponseWithJson(call, expected, OllamaStatus.class, mapper);
        }

        OllamaCompletionRequest request = OllamaCompletionRequest
                .builder()
                .name("testModel")
                .modelFile("FROM mistral\n PARAMETER temperature 1\n PARAMETER num_ctx 4096\n SYSTEM You are Mario from super mario bros, acting as an assistant.")
                .stream(false)
                .build();

        OllamaStatus response = Merlin.builder()
                .addService(service)
                .build()
                .getOllamaService()
                .createModel(request)
                .join();
        assertEquals("success", response.getStatus(), "status should be success");

        System.out.println(JsonPrinter.print(response));
    }

    @Test
    void checkBlob() {
        if (config.useMock()) {
            Call<Void> call = mock(Call.class);
            when(api.checkBlob(anyString())).thenReturn(call);
            TestHelper.setupFailedAsyncResponse(call, new CompletionException(new LlmApiException("Fake exception", 404, "No error body")));
        }

        CompletableFuture<Void> future = Merlin.builder()
                .addService(service)
                .build()
                .getOllamaService()
                .checkBlob("sha256:20be4dc9fc870a36479d8223fec6fca8486003829b4de3bff3bd272679256946");
        try {
            future.join();
            assertTrue(future.isDone(), "something's wrong");
        } catch (CompletionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof LlmApiException llmApiException) {
                assertEquals(404, llmApiException.statusCode(), "status code should be 404");
            } else {
                throw e;
            }
        }
    }

    @Test
    void createBlob() throws IOException, NoSuchAlgorithmException {
        File testFile = TestHelper.getFile("ollama/model.bin");
        String digest = TestHelper.getSHA256(testFile);

        if (config.useMock()) {
            Call<Void> call = mock(Call.class);
            when(api.createBlob(anyString(), any(RequestBody.class))).thenReturn(call);
            TestHelper.setupSuccessfulAsyncResponseWithJson(call, null, Void.class, mapper);
        }

        CompletableFuture<Void> future = Merlin.builder()
                .addService(service)
                .build()
                .getOllamaService()
                .createBlob("sha256:" + digest, testFile);
        try {
            future.join();
            assertTrue(future.isDone(), "something's wrong");
        } catch (CompletionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof LlmApiException a) {
                assertEquals(400, a.statusCode(), "status code should be 400");
            } else {
                throw e;
            }
        }
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

        OllamaCompletionRequest request = OllamaCompletionRequest.builder()
                .source("mistral")
                .destination("mistral-backup")
                .build();
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