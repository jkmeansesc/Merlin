package org.haifan.merlin.service;

import org.haifan.merlin.client.Merlin;
import org.haifan.merlin.internal.constants.Provider;
import org.haifan.merlin.internal.utils.DefaultObjectMapper;
import org.haifan.merlin.model.ollama.*;
import org.haifan.merlin.utils.TestConfig;
import org.haifan.merlin.utils.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import retrofit2.HttpException;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class OllamaServiceTest {

    private LlmConfig config;
    private OllamaService service;

    @BeforeEach
    void setUp() {
        if (TestConfig.useMock()) {
            this.config = new LlmConfig(Provider.OLLAMA, "https://ollama.wiremockapi.cloud", null);
        } else {
            this.config = new LlmConfig(Provider.OLLAMA, "http://10.1.1.12:11434", null);
        }
        this.config.setTimeOut(Duration.ofSeconds(60));
        this.config.setLogLevel(LlmConfig.Level.BODY);
        service = new OllamaService(config);
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

        OllamaCompletionRequest request = OllamaCompletionRequest
                .builder()
                .model("mistral")
                .prompt("Are you there? Say yes if yes and no if no.")
                .build();

        List<String> receivedChunks = new ArrayList<>();
        AtomicBoolean isDone = new AtomicBoolean(false);
        AtomicReference<Throwable> e = new AtomicReference<>();
        CompletableFuture<Void> future = new CompletableFuture<>();

        Merlin.builder()
                .addService(service)
                .build()
                .getService(OllamaService.class)
                .streamCompletion(request)
                .start(
                        chunk -> {
                            System.out.println("Received chunk: " + chunk.getResponse());
                            receivedChunks.add(chunk.getResponse());
                        },
                        error -> {
                            System.err.println("Error occurred: " + error.getMessage());
                            e.set(error);
                            future.completeExceptionally(error);
                        },
                        () -> {
                            System.out.println("Streaming completed");
                            isDone.set(true);
                            future.complete(null);
                        }
                );
        future.join();
        if (e.get() != null) {
            fail("Stream completed with an error: " + e.get().getMessage());
        }
        assertTrue(isDone.get(), "Stream should complete successfully");
        assertFalse(receivedChunks.isEmpty(), "Should receive at least one chunk");
        System.out.println(String.join("", receivedChunks));
    }

    @Test
    void createCompletion() {

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
        System.out.println(DefaultObjectMapper.print(response));
    }

    @Test
    void createChatCompletion() {

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
        System.out.println(DefaultObjectMapper.print(response));
    }

    @Test
    @Disabled("Not mocked")
    void streamChatCompletion() {

        List<OllamaMessage> messages = new ArrayList<>();

        OllamaMessage message = OllamaMessage.builder().role("user").content("Are you online?").build();
        messages.add(message);

        OllamaCompletionRequest request = OllamaCompletionRequest
                .builder()
                .model("mistral")
                .messages(messages)
                .build();

        List<String> receivedChunks = new ArrayList<>();
        AtomicBoolean isDone = new AtomicBoolean(false);
        AtomicReference<Throwable> e = new AtomicReference<>();
        CompletableFuture<Void> future = new CompletableFuture<>();

        Merlin.builder()
                .addService(service)
                .build()
                .getService(OllamaService.class)
                .streamChatCompletion(request)
                .start(
                        chunk -> {
                            System.out.println("Received chunk: " + chunk.getMessage().getContent());
                            receivedChunks.add(chunk.getMessage().getContent());
                        },
                        error -> {
                            System.err.println("Error occurred: " + error.getMessage());
                            e.set(error);
                            future.completeExceptionally(error);
                        },
                        () -> {
                            System.out.println("Streaming completed");
                            isDone.set(true);
                            future.complete(null);
                        }
                );
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

        OllamaCompletionRequest request = OllamaCompletionRequest
                .builder()
                .name("testModelStream")
                .modelFile("FROM mistral\n PARAMETER temperature 1\n PARAMETER num_ctx 4096\n SYSTEM You are Mario from super mario bros, acting as an assistant.")
                .build();

        List<String> receivedChunks = new ArrayList<>();
        AtomicBoolean isDone = new AtomicBoolean(false);
        AtomicReference<Throwable> e = new AtomicReference<>();
        CompletableFuture<Void> future = new CompletableFuture<>();

        Merlin.builder()
                .addService(service)
                .build()
                .getService(OllamaService.class)
                .createModelStream(request)
                .start(
                        chunk -> {
                            System.out.println("Received chunk: " + chunk);
                            receivedChunks.add(chunk.getStatus());
                        },
                        error -> {
                            System.err.println("Error occurred: " + error.getMessage());
                            e.set(error);
                            future.completeExceptionally(error);
                        },
                        () -> {
                            System.out.println("Streaming completed");
                            isDone.set(true);
                            future.complete(null);
                        }
                );
        future.join();
        if (e.get() != null) {
            fail("Stream completed with an error: " + e.get().getMessage());
        }
        assertTrue(isDone.get(), "Stream should complete successfully");
        assertFalse(receivedChunks.isEmpty(), "Should receive at least one chunk");
    }

    @Test
    void createModel() {
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
        System.out.println(DefaultObjectMapper.print(response));
    }

    @Test
    void checkBlob() {
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
        OllamaModelList response = Merlin.builder()
                .addService(service)
                .build()
                .getService(OllamaService.class)
                .listModels()
                .join();
        assertNotNull(response.getModels());
        System.out.println(DefaultObjectMapper.print(response));
    }

    @Test
    void showModelInfo() {
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
        System.out.println(DefaultObjectMapper.print(response));
    }

    @Test
    void copyModel() {
        OllamaCompletionRequest request = OllamaCompletionRequest.builder()
                .source("mistral")
                .destination("mistral-backup")
                .build();

        CompletableFuture<Void> future = Merlin.builder()
                .addService(service)
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
    @Disabled("Not mocked")
    void pullModelStream() {
        OllamaCompletionRequest request = OllamaCompletionRequest.builder()
                .name("llama3")
                .build();

        List<String> receivedChunks = new ArrayList<>();
        AtomicBoolean isDone = new AtomicBoolean(false);
        AtomicReference<Throwable> e = new AtomicReference<>();
        CompletableFuture<Void> future = new CompletableFuture<>();

        Merlin.builder()
                .addService(service)
                .build()
                .getService(OllamaService.class)
                .pullModelStream(request)
                .start(
                        chunk -> {
                            System.out.println("Received chunk: " + chunk);
                            receivedChunks.add(chunk.getStatus());
                        },
                        error -> {
                            System.err.println("Error occurred: " + error.getMessage());
                            e.set(error);
                            future.completeExceptionally(error);
                        },
                        () -> {
                            System.out.println("Streaming completed");
                            isDone.set(true);
                            future.complete(null);
                        }
                );
        future.join();
        if (e.get() != null) {
            fail("Stream completed with an error: " + e.get().getMessage());
        }
        assertTrue(isDone.get(), "Stream should complete successfully");
        assertFalse(receivedChunks.isEmpty(), "Should receive at least one chunk");
    }

    @Test
    void pullModel() {
        OllamaCompletionRequest request = OllamaCompletionRequest.builder()
                .name("llama3")
                .build();
        OllamaStatus response = Merlin.builder()
                .addService(service)
                .build()
                .getService(OllamaService.class)
                .pullModel(request)
                .join();
        assertEquals("success", response.getStatus(), "status should be success");
    }

    @Test
    @Disabled("Not mocked")
    void pushModelStream() {
        OllamaCompletionRequest request = OllamaCompletionRequest.builder()
                .name("test/test:latest")
                .build();

        List<String> receivedChunks = new ArrayList<>();
        AtomicBoolean isDone = new AtomicBoolean(false);
        AtomicReference<Throwable> e = new AtomicReference<>();
        CompletableFuture<Void> future = new CompletableFuture<>();
        Merlin.builder()
                .addService(service)
                .build()
                .getService(OllamaService.class)
                .pushModelStream(request)
                .start(
                        chunk -> {
                            System.out.println("Received chunk: " + chunk);
                            receivedChunks.add(chunk.getStatus());
                        },
                        error -> {
                            System.err.println("Error occurred: " + error.getMessage());
                            e.set(error);
                            future.completeExceptionally(error);
                        },
                        () -> {
                            System.out.println("Streaming completed");
                            isDone.set(true);
                            future.complete(null);
                        }
                );

        future.join();
        if (e.get() != null) {
            fail("Stream completed with an error: " + e.get().getMessage());
        }
        assertTrue(isDone.get(), "Stream should complete successfully");
        assertFalse(receivedChunks.isEmpty(), "Should receive at least one chunk");
    }

    @Test
    void pushModel() {
        OllamaCompletionRequest request = OllamaCompletionRequest.builder()
                .name("test/test:latest")
                .build();

        OllamaStatus response = Merlin.builder()
                .addService(service)
                .build()
                .getService(OllamaService.class)
                .pushModel(request)
                .join();
        assertEquals("success", response.getStatus(), "status should be success");
    }

    @Test
    void createEmbedding() {
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
        System.out.println(DefaultObjectMapper.print(response));
    }


    @Test
    void listRunning() {
        OllamaModelList response = Merlin.builder()
                .addService(service)
                .build()
                .getService(OllamaService.class)
                .listRunning()
                .join();
        assertNotNull(response.getModels());
        System.out.println(DefaultObjectMapper.print(response));
    }
}