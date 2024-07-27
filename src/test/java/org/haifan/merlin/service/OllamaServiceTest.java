package org.haifan.merlin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.haifan.merlin.internal.api.OllamaApi;
import org.haifan.merlin.client.Merlin;
import org.haifan.merlin.internal.constants.IanaMediaType;
import org.haifan.merlin.internal.constants.Provider;
import org.haifan.merlin.internal.interceptors.OllamaInterceptor;
import org.haifan.merlin.model.ollama.*;
import org.haifan.merlin.internal.utils.DefaultObjectMapper;
import org.haifan.merlin.internal.utils.JsonPrinter;
import org.haifan.merlin.utils.TestConfig;
import org.haifan.merlin.utils.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;

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
    private LlmConfig config;
    private final OllamaInterceptor interceptor = new OllamaInterceptor();
    private OllamaService service;
    private final ObjectMapper mapper = DefaultObjectMapper.create();

    @BeforeEach
    void setUp() {
        if (TestConfig.useMock()) {
            service = new OllamaService(api, config, interceptor);
        } else {
            service = new OllamaService();
        }
    }

    @Test
    void test_all_constructors_can_be_initialized() {

        LlmService service_1 = new OllamaService();
        assertNotNull(service_1.getClient());
        assertNotNull(service_1.getMapper());
        assertNotNull(service_1.getRetrofit());
        assertNotNull(service_1.getConfig());

        this.config = new LlmConfig(Provider.OLLAMA, "test baseUrl", "test token");
        LlmService service_2 = new OllamaService(config);
        assertEquals("test baseurl", service_2.getConfig().getBaseUrl(), "Wrong baseUrl");
        assertEquals("test token", service_2.getConfig().getToken(), "Wrong token");

    }

    @Test
    void streamCompletion() {

        OllamaCompletionRequest request = OllamaCompletionRequest
                .builder()
                .model("mistral")
                .prompt("Are you there? Say yes if yes and no if no.")
                .build();
        CompletableFuture<Void> future = new CompletableFuture<>();
        Merlin.builder()
                .addService(service)
                .build()
                .getService(OllamaService.class)
                .streamCompletion(request)
                .start(
                        chunk -> {
                        },
                        error -> {
                            System.err.println("Error occurred: " + error.getMessage());
                            future.completeExceptionally(error);
                        },
                        () -> {
                            System.out.println("Streaming completed");
                            future.complete(null);
                        }
                );
        future.join();
    }

    @Test
    void createCompletion() {

        if (TestConfig.useMock()) {
            String expected = TestHelper.read("ollama/ollama_completion_no_stream.json");
            Call<OllamaCompletion> call = mock(Call.class);
            when(api.createCompletion(any(OllamaCompletionRequest.class))).thenReturn(call);
            TestHelper.setupSuccessfulAsyncResponseWithJson(call, expected, OllamaCompletion.class, mapper);
        }

        OllamaCompletionRequest request = OllamaCompletionRequest
                .builder()
                .model("mistral")
                .prompt("Are you there?")
                .build();

        OllamaCompletion response = Merlin.builder()
                .addService(service)
                .build()
                .getService(OllamaService.class)
                .createCompletion(request)
                .join();

        assertTrue(response.getDone(), "Non streaming response should always return true");
        assertEquals("stop", response.getDoneReason(), "Non streaming response should always return stop");
        assertNotNull(response.getResponse(), "Response should not be null");
        System.out.println(JsonPrinter.print(response));
    }

    @Test
    void createChatCompletion() {

        if (TestConfig.useMock()) {
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
                .build();

        OllamaCompletion response = Merlin.builder()
                .addService(service)
                .build()
                .getService(OllamaService.class)
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
        if (TestConfig.useMock()) {
            String expected = TestHelper.read("ollama/ollama_status_success.json");
            Call<OllamaStatus> call = mock(Call.class);
            when(api.createModel(any(OllamaCompletionRequest.class))).thenReturn(call);
            TestHelper.setupSuccessfulAsyncResponseWithJson(call, expected, OllamaStatus.class, mapper);
        }

        OllamaCompletionRequest request = OllamaCompletionRequest
                .builder()
                .name("testModel")
                .modelFile("FROM mistral\n PARAMETER temperature 1\n PARAMETER num_ctx 4096\n SYSTEM You are Mario from super mario bros, acting as an assistant.")
                .build();

        OllamaStatus response = Merlin.builder()
                .addService(service)
                .build()
                .getService(OllamaService.class)
                .createModel(request)
                .join();
        assertEquals("success", response.getStatus(), "status should be success");

        System.out.println(JsonPrinter.print(response));
    }

    @Test
    void checkBlob() {
        if (TestConfig.useMock()) {
            Call<Void> call = mock(Call.class);
            when(api.checkBlob(anyString())).thenReturn(call);
            TestHelper.setupFailedAsyncResponse(call, new CompletionException(new HttpException(Response.error(404, ResponseBody.create("", MediaType.get(IanaMediaType.JSON))))));
        }

        CompletableFuture<Void> future = Merlin.builder()
                .addService(service)
                .build()
                .getService(OllamaService.class)
                .checkBlob("sha256:20be4dc9fc870a36479d8223fec6fca8486003829b4de3bff3bd272679256946");
        try {
            future.join();
            assertTrue(future.isDone(), "something's wrong");
        } catch (CompletionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof HttpException a) {
                assertEquals(404, a.code(), "status code should be 404");
            } else {
                throw e;
            }
        }
    }

    @Test
    void createBlob() throws IOException, NoSuchAlgorithmException {
        File testFile = TestHelper.getFile("ollama/model.bin");
        String digest = TestHelper.getSHA256(testFile);

        if (TestConfig.useMock()) {
            Call<Void> call = mock(Call.class);
            when(api.createBlob(anyString(), any(RequestBody.class))).thenReturn(call);
            TestHelper.setupSuccessfulAsyncResponseWithJson(call, null, Void.class, mapper);
        }

        CompletableFuture<Void> future = Merlin.builder()
                .addService(service)
                .build()
                .getService(OllamaService.class)
                .createBlob("sha256:" + digest, testFile);
        try {
            future.join();
            assertTrue(future.isDone(), "something's wrong");
        } catch (CompletionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof HttpException a) {
                assertEquals(400, a.code(), "status code should be 400");
            } else {
                throw e;
            }
        }
    }

    @Test
    void listModels() {
        if (TestConfig.useMock()) {
            String expected = TestHelper.read("ollama/ollama_model_list.json");
            Call<OllamaModelList> call = mock(Call.class);
            when(api.listModels()).thenReturn(call);
            TestHelper.setupSuccessfulAsyncResponseWithJson(call, expected, OllamaModelList.class, mapper);
        }

        OllamaModelList response = Merlin.builder()
                .addService(service)
                .build()
                .getService(OllamaService.class)
                .listModels()
                .join();
        assertNotNull(response.getModels());
        System.out.println(JsonPrinter.print(response));
    }

    @Test
    void showModelInfo() {
        if (TestConfig.useMock()) {
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
                .getService(OllamaService.class)
                .showModelInfo(request)
                .join();
        assertNotNull(response.getModelInfo());
        System.out.println(JsonPrinter.print(response));
    }

    @Test
    void copyModel() {
        if (TestConfig.useMock()) {
            Call<Void> call = mock(Call.class);
            when(api.copyModel(any(OllamaCompletionRequest.class))).thenReturn(call);
            TestHelper.setupSuccessfulAsyncResponseWithJson(call, null, Void.class, mapper);
        }

        OllamaCompletionRequest request = OllamaCompletionRequest.builder()
                .source("mistral")
                .destination("mistral-backup")
                .build();

        CompletableFuture<Void> future = Merlin.builder()
                .ollama()
                .build()
                .getService(OllamaService.class)
                .copyModel(request);

        try {
            future.join();
            assertTrue(future.isDone(), "something's wrong");
        } catch (CompletionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof HttpException a) {
                assertEquals(404, a.code(), "status code should be 404");
            } else {
                throw e;
            }
        }

    }

    @Test
    void deleteModel() {

        if (TestConfig.useMock()) {
            Call<Void> call = mock(Call.class);
            when(api.deleteModel(any(OllamaCompletionRequest.class))).thenReturn(call);
            TestHelper.setupSuccessfulAsyncResponseWithJson(call, null, Void.class, mapper);
        }

        OllamaCompletionRequest request = OllamaCompletionRequest.builder()
                .name("testModel:latest")
                .build();

        CompletableFuture<Void> future = Merlin.builder()
                .addService(service)
                .build()
                .getService(OllamaService.class)
                .deleteModel(request);

        try {
            future.join();
            assertTrue(future.isDone(), "something's wrong");
        } catch (CompletionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof HttpException a) {
                assertEquals(500, a.code(), "status code should be 404 by documentation but actually it returns 500, so 500 is expected.");
            } else {
                throw e;
            }
        }
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
        if (TestConfig.useMock()) {
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
                .getService(OllamaService.class)
                .createEmbedding(request)
                .join();
        assertNotNull(response.getEmbedding());
        System.out.println(JsonPrinter.print(response));
    }


    @Test
    void listRunning() {
        if (TestConfig.useMock()) {
            String expected = TestHelper.read("ollama/ollama_model_list.json");
            Call<OllamaModelList> call = mock(Call.class);
            when(api.listRunning()).thenReturn(call);
            TestHelper.setupSuccessfulAsyncResponseWithJson(call, expected, OllamaModelList.class, mapper);
        }

        OllamaModelList response = Merlin.builder()
                .addService(service)
                .build()
                .getService(OllamaService.class)
                .listRunning()
                .join();
        assertNotNull(response.getModels());
        System.out.println(JsonPrinter.print(response));
    }
}