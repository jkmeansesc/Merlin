package org.merlin.service;

import org.merlin.annotations.UseRelay;
import org.merlin.annotations.UseWireMock;
import org.merlin.internal.constants.Provider;
import org.merlin.internal.utils.DefaultObjectMapper;
import org.merlin.model.ollama.*;
import org.merlin.utils.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import retrofit2.HttpException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class OllamaServiceTest {

    private LlmConfig config;
    private OllamaService service;

    @BeforeEach
    void setUp(TestInfo testInfo) {

        String baseUrl = "http://10.1.1.12:11434";
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
                baseUrl = "https://ollama.wiremockapi.cloud/";
            } else if (method.isAnnotationPresent(UseRelay.class)) {
                UseRelay annotation = method.getAnnotation(UseRelay.class);
                baseUrl = annotation.value();
                apiKey = p.getProperty("api.key");
            }
        }

        this.config = new LlmConfig(Provider.OPENAI, baseUrl, apiKey);
        this.config.setTimeOut(Duration.ofSeconds(60));
        this.config.setLogLevel(LlmConfig.Level.BASIC);
        service = Merlin.builder().ollama(config).build().getService(OllamaService.class);
    }

    @Test
    void test_all_constructors_can_be_initialized() {

        LlmService service_1 = new OllamaService();
        assertNotNull(service_1.getClient());
        assertNotNull(service_1.getMapper());
        assertNotNull(service_1.getRetrofit());
        assertNotNull(service_1.getConfig());

        this.config = new LlmConfig(Provider.OLLAMA, "https://test.baseUrl.com", "test token");
        LlmService service_2 = new OllamaService(config);
        assertEquals("https://test.baseUrl.com", service_2.getConfig().getBaseUrl(), "Wrong baseUrl");
        assertEquals("test token", service_2.getConfig().getToken(), "Wrong token");

    }

    @Test
    @Disabled("Not mocked")
    void streamCompletion() {
        OllamaCompletionRequest request = OllamaCompletionRequest.builder().model("mistral").prompt("Are you there? Say yes if yes and no if no.").build();
        List<String> receivedChunks = new ArrayList<>();
        AtomicBoolean isDone = new AtomicBoolean(false);
        AtomicReference<Throwable> e = new AtomicReference<>();
        CompletableFuture<Void> future = new CompletableFuture<>();
        service.streamCompletion(request).start(chunk -> {
            System.out.println("Received chunk: " + chunk.getResponse());
            receivedChunks.add(chunk.getResponse());
        }, error -> {
            System.err.println("Error occurred: " + error.getMessage());
            e.set(error);
            future.completeExceptionally(error);
        }, () -> {
            System.out.println("Streaming completed");
            isDone.set(true);
            future.complete(null);
        });
        future.join();
        if (e.get() != null) {
            fail("Stream completed with an error: " + e.get().getMessage());
        }
        assertTrue(isDone.get(), "Stream should complete successfully");
        assertFalse(receivedChunks.isEmpty(), "Should receive at least one chunk");
        System.out.println(String.join("", receivedChunks));
    }

    @Test
    @UseWireMock
    void createCompletion() {

        OllamaCompletionRequest request = OllamaCompletionRequest.builder().model("mistral").prompt("Are you there?").build();

        OllamaCompletion response = service.createCompletion(request).join();

        assertTrue(response.getDone(), "Non streaming response should always return true");
        assertEquals("stop", response.getDoneReason(), "Non streaming response should always return stop");
        assertNotNull(response.getResponse(), "Response should not be null");
        System.out.println(DefaultObjectMapper.print(response));
    }

    @Test
    @UseWireMock
    void createChatCompletion() {

        List<OllamaMessage> messages = new ArrayList<>();
        OllamaMessage message = new OllamaMessage();
        message.setRole("user");
        message.setContent("Are you online?");
        messages.add(message);

        OllamaCompletionRequest request = OllamaCompletionRequest.builder().model("mistral").messages(messages).build();

        OllamaCompletion response = service.createChatCompletion(request).join();

        assertTrue(response.getDone(), "Non streaming response should always return true");
        assertEquals("stop", response.getDoneReason(), "Non streaming response should always return stop");
        assertNotNull(response.getMessage(), "Response should not be null");
        System.out.println(DefaultObjectMapper.print(response));
    }

    @Test
    @Disabled("Not mocked")
    void streamChatCompletion() {

        List<OllamaMessage> messages = new ArrayList<>();

        OllamaMessage message = OllamaMessage.builder().role("user").content("Are you online?").build();
        messages.add(message);

        OllamaCompletionRequest request = OllamaCompletionRequest.builder().model("mistral").messages(messages).build();

        List<String> receivedChunks = new ArrayList<>();
        AtomicBoolean isDone = new AtomicBoolean(false);
        AtomicReference<Throwable> e = new AtomicReference<>();
        CompletableFuture<Void> future = new CompletableFuture<>();

        service.streamChatCompletion(request).start(chunk -> {
            System.out.println("Received chunk: " + chunk.getMessage().getContent());
            receivedChunks.add(chunk.getMessage().getContent());
        }, error -> {
            System.err.println("Error occurred: " + error.getMessage());
            e.set(error);
            future.completeExceptionally(error);
        }, () -> {
            System.out.println("Streaming completed");
            isDone.set(true);
            future.complete(null);
        });
        future.join();
        if (e.get() != null) {
            fail("Stream completed with an error: " + e.get().getMessage());
        }
        assertTrue(isDone.get(), "Stream should complete successfully");
        assertFalse(receivedChunks.isEmpty(), "Should receive at least one chunk");
        System.out.println(String.join("", receivedChunks));

    }

    @Test
    @Disabled("Not mocked")
    void createModelStream() {

        OllamaCompletionRequest request = OllamaCompletionRequest.builder().name("testModelStream").modelFile("FROM mistral\n PARAMETER temperature 1\n PARAMETER num_ctx 4096\n SYSTEM You are Mario from super mario bros, acting as an assistant.").build();

        List<String> receivedChunks = new ArrayList<>();
        AtomicBoolean isDone = new AtomicBoolean(false);
        AtomicReference<Throwable> e = new AtomicReference<>();
        CompletableFuture<Void> future = new CompletableFuture<>();

        service.createModelStream(request).start(chunk -> {
            System.out.println("Received chunk: " + chunk);
            receivedChunks.add(chunk.getStatus());
        }, error -> {
            System.err.println("Error occurred: " + error.getMessage());
            e.set(error);
            future.completeExceptionally(error);
        }, () -> {
            System.out.println("Streaming completed");
            isDone.set(true);
            future.complete(null);
        });
        future.join();
        if (e.get() != null) {
            fail("Stream completed with an error: " + e.get().getMessage());
        }
        assertTrue(isDone.get(), "Stream should complete successfully");
        assertFalse(receivedChunks.isEmpty(), "Should receive at least one chunk");
    }

    @Test
    @UseWireMock
    void createModel() {
        OllamaCompletionRequest request = OllamaCompletionRequest.builder().name("testModel").modelFile("FROM mistral\n PARAMETER temperature 1\n PARAMETER num_ctx 4096\n SYSTEM You are Mario from super mario bros, acting as an assistant.").build();

        OllamaStatus response = service.createModel(request).join();
        assertEquals("success", response.getStatus(), "status should be success");
        System.out.println(DefaultObjectMapper.print(response));
    }

    @Test
    @UseWireMock
    void checkBlob() {
        CompletableFuture<Void> future = service.checkBlob("sha256:20be4dc9fc870a36479d8223fec6fca8486003829b4de3bff3bd272679256946");
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
    @UseWireMock
    void createBlob() throws IOException, NoSuchAlgorithmException {
        File testFile = TestHelper.getFile("ollama/model.bin");
        String digest = TestHelper.getSHA256(testFile);

        CompletableFuture<Void> future = service.createBlob("sha256:" + digest, testFile);
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
    @UseWireMock
    void listModels() {
        OllamaModelList response = service.listModels().join();
        assertNotNull(response.getModels());
        System.out.println(DefaultObjectMapper.print(response));
    }

    @Test
    @UseWireMock
    void showModelInfo() {
        OllamaCompletionRequest request = OllamaCompletionRequest.builder().name("mistral").build();
        OllamaModel response = service.showModelInfo(request).join();
        assertNotNull(response.getModelInfo());
        System.out.println(DefaultObjectMapper.print(response));
    }

    @Test
    @UseWireMock
    void copyModel() {
        OllamaCompletionRequest request = OllamaCompletionRequest.builder().source("mistral").destination("mistral-backup").build();

        CompletableFuture<Void> future = service.copyModel(request);

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
    @UseWireMock
    void deleteModel() {
        OllamaCompletionRequest request = OllamaCompletionRequest.builder().name("testModel:latest").build();
        CompletableFuture<Void> future = service.deleteModel(request);
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
    @Disabled("Not mocked")
    void pullModelStream() {
        OllamaCompletionRequest request = OllamaCompletionRequest.builder().name("llama3").build();

        List<String> receivedChunks = new ArrayList<>();
        AtomicBoolean isDone = new AtomicBoolean(false);
        AtomicReference<Throwable> e = new AtomicReference<>();
        CompletableFuture<Void> future = new CompletableFuture<>();

        service.pullModelStream(request).start(chunk -> {
            System.out.println("Received chunk: " + chunk);
            receivedChunks.add(chunk.getStatus());
        }, error -> {
            System.err.println("Error occurred: " + error.getMessage());
            e.set(error);
            future.completeExceptionally(error);
        }, () -> {
            System.out.println("Streaming completed");
            isDone.set(true);
            future.complete(null);
        });
        future.join();
        if (e.get() != null) {
            fail("Stream completed with an error: " + e.get().getMessage());
        }
        assertTrue(isDone.get(), "Stream should complete successfully");
        assertFalse(receivedChunks.isEmpty(), "Should receive at least one chunk");
    }

    @Test
    @UseWireMock
    void pullModel() {
        OllamaCompletionRequest request = OllamaCompletionRequest.builder().name("llama3").build();
        OllamaStatus response = service.pullModel(request).join();
        assertEquals("success", response.getStatus(), "status should be success");
    }

    @Test
    @Disabled("Not mocked")
    void pushModelStream() {
        OllamaCompletionRequest request = OllamaCompletionRequest.builder().name("test/test:latest").build();

        List<String> receivedChunks = new ArrayList<>();
        AtomicBoolean isDone = new AtomicBoolean(false);
        AtomicReference<Throwable> e = new AtomicReference<>();
        CompletableFuture<Void> future = new CompletableFuture<>();
        service.pushModelStream(request).start(chunk -> {
            System.out.println("Received chunk: " + chunk);
            receivedChunks.add(chunk.getStatus());
        }, error -> {
            System.err.println("Error occurred: " + error.getMessage());
            e.set(error);
            future.completeExceptionally(error);
        }, () -> {
            System.out.println("Streaming completed");
            isDone.set(true);
            future.complete(null);
        });

        future.join();
        if (e.get() != null) {
            fail("Stream completed with an error: " + e.get().getMessage());
        }
        assertTrue(isDone.get(), "Stream should complete successfully");
        assertFalse(receivedChunks.isEmpty(), "Should receive at least one chunk");
    }

    @Test
    @UseWireMock
    void pushModel() {
        OllamaCompletionRequest request = OllamaCompletionRequest.builder().name("test/test:latest").build();

        OllamaStatus response = service.pushModel(request).join();
        assertEquals("success", response.getStatus(), "status should be success");
    }

    @Test
    @UseWireMock
    void createEmbedding() {
        OllamaCompletionRequest request = OllamaCompletionRequest.builder().model("mistral").prompt("Here is an article about llamas...").build();
        OllamaEmbedding response = service.createEmbedding(request).join();
        assertNotNull(response.getEmbedding());
        System.out.println(DefaultObjectMapper.print(response));
    }


    @Test
    @UseWireMock
    void listRunning() {
        OllamaModelList response = service.listRunning().join();
        assertNotNull(response.getModels());
        System.out.println(DefaultObjectMapper.print(response));
    }
}