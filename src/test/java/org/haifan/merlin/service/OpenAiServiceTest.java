package org.haifan.merlin.service;

import org.haifan.merlin.client.Merlin;
import org.haifan.merlin.internal.constants.Provider;
import org.haifan.merlin.internal.utils.DefaultObjectMapper;
import org.haifan.merlin.model.openai.*;
import org.haifan.merlin.model.openai.Function;
import org.haifan.merlin.model.openai.endpoints.audio.SpeechRequest;
import org.haifan.merlin.model.openai.endpoints.chat.*;
import org.haifan.merlin.model.openai.endpoints.embeddings.EmbeddingRequest;
import org.haifan.merlin.model.openai.endpoints.images.ImageRequest;
import org.haifan.merlin.model.openai.endpoints.moderations.ModerationRequest;
import org.haifan.merlin.utils.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

class OpenAiServiceTest {

    private LlmConfig config;
    private OpenAiService service;

    @BeforeEach
    void setUp() {
        if (TestConfig.useMock()) {
            this.config = new LlmConfig(Provider.OPENAI, "https://openai.wiremockapi.cloud/", null);
        } else {
            this.config = new LlmConfig(Provider.OPENAI, "https://api.chatanywhere.cn", "sk-wZ2iR46dia1VcsgA3WkLpelIVMcftmsxxvF4Ebzcm0dKFkgL");
        }
        this.config.setLogLevel(LlmConfig.Level.BODY);
        this.service = new OpenAiService(config);
    }

    @Test
    void test_constructor_can_be_initialized() {
        LlmService service_1 = new OpenAiService();
        assertNotNull(service_1.getClient());
        assertNotNull(service_1.getMapper());
        assertNotNull(service_1.getRetrofit());
        assertNotNull(service_1.getConfig());

        this.config = new LlmConfig(Provider.OPENAI, "https://test.baseUrl.com", "test token");
        LlmService service_2 = new OpenAiService(config);
        assertEquals("https://test.baseUrl.com", service_2.getConfig().getBaseUrl(), "Wrong baseUrl");
        assertEquals("test token", service_2.getConfig().getToken(), "Wrong token");
    }

    @Nested
    class ChatTest {
        @Test
        void test_all_request_fields_can_be_set() {
            List<OpenAiMessage> messages = new ArrayList<>();
            messages.add(new OpenAiMessage(OpenAiMessage.Role.SYSTEM, "system message"));
            messages.add(new OpenAiMessage(OpenAiMessage.Role.USER, "user message"));
            messages.add(new OpenAiMessage(OpenAiMessage.Role.TOOL, "tool message", "toolCallId"));
            messages.add(new OpenAiMessage(OpenAiMessage.Role.ASSISTANT, "assistant message"));

            assertEquals("system", messages.get(0).getRoleAsString(), "Wrong role");
            assertEquals("user", messages.get(1).getRoleAsString(), "Wrong role");
            assertEquals("tool", messages.get(2).getRoleAsString(), "Wrong role");
            assertEquals("assistant", messages.get(3).getRoleAsString(), "Wrong role");

            List<Tool> tools = new ArrayList<>();
            tools.add(new Tool("auto", new Function("function")));

            Map<String, Integer> logitBias = new HashMap<>();
            logitBias.put("logitBias", 1);

            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .messages(messages)
                    .model("model")
                    .frequencyPenalty(0.1)
                    .logitBias(logitBias)
                    .logprobs(false)
                    .topLogprobs(1)
                    .maxTokens(400)
                    .n(5)
                    .presencePenalty(1.1)
                    .responseFormat(new ResponseFormat("type"))
                    .seed(1)
                    .serviceTier("auto")
                    .stop(Arrays.stream(new String[]{"1", "2"}).toList())
                    .stream(false)
                    .streamOptions(new ChatCompletionRequest.StreamOptions())
                    .temperature(11.0)
                    .topP(10.1)
                    .tools(tools)
                    .toolChoice(new ToolChoice("auto"))
                    .parallelToolCalls(false)
                    .user("user")
                    .build();

            assertEquals("model", request.getModel());
            System.out.println(DefaultObjectMapper.print(request));
        }

        @Test
        void createChatCompletion() {
            List<OpenAiMessage> messages = new ArrayList<>();
            messages.add(new OpenAiMessage(OpenAiMessage.Role.USER, "Are you there?"));

            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model("gpt-4o")
                    .messages(messages)
                    .build();
            ChatCompletion response = Merlin.builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .createChatCompletion(request)
                    .join();

            assertNotNull(response.getChoices().get(0).getMessage());
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @Disabled("Not mocked")
        void streamChatCompletion() {
            List<OpenAiMessage> messages = new ArrayList<>();
            messages.add(new OpenAiMessage(OpenAiMessage.Role.USER, "Hi, are you there?"));

            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model("gpt-3.5-turbo-0125")
                    .messages(messages)
                    .build();

            AtomicBoolean isDone = new AtomicBoolean(false);
            AtomicReference<Throwable> e = new AtomicReference<>();
            CompletableFuture<Void> future = new CompletableFuture<>();

            Merlin.builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .streamChatCompletion(request)
                    .start(
                            chunk -> {
                                System.out.println("Received chunk: " + chunk);
                                OpenAiMessage message = chunk.getChoices().get(0).getMessage();
                                messages.add(message);
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
            assertTrue(messages.size() > 1, "Should receive at least one chunk");
            System.out.println(DefaultObjectMapper.print(messages));
        }
    }

    @Nested
    class TestEmbedding {
        @Test
        void createEmbeddings() {

            // Single string
            EmbeddingRequest request1 = EmbeddingRequest.builder()
                    .input("This is a test string")
                    .dimensions(1)
                    .encodingFormat("base64")
                    .model("text-embedding-3")
                    .build();
            System.out.println(DefaultObjectMapper.print(request1));

            // Array of strings
            List<String> stringList = Arrays.asList("String 1", "String 2", "String 3");
            EmbeddingRequest request2 = EmbeddingRequest.builder()
                    .input(stringList)
                    .model("text-embedding-ada-002")
                    .build();

            System.out.println(DefaultObjectMapper.print(request2));

            // Array of integers
            List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5);
            EmbeddingRequest request3 = EmbeddingRequest.builder()
                    .input(intList)
                    .model("text-embedding-ada-002")
                    .build();

            System.out.println(DefaultObjectMapper.print(request3));

            // Array of arrays of integers
            List<List<Integer>> nestedList = Arrays.asList(
                    Arrays.asList(1, 2, 3),
                    Arrays.asList(4, 5, 6),
                    Arrays.asList(7, 8, 9)
            );

            EmbeddingRequest request4 = EmbeddingRequest.builder()
                    .input(nestedList)
                    .model("text-embedding-ada-002")
                    .build();

            System.out.println(DefaultObjectMapper.print(request4));

            EmbeddingRequest request5 = EmbeddingRequest.builder()
                    .input(new Integer[]{1, 2, 3})
                    .model("text-embedding-ada-002")
                    .build();

            System.out.println(DefaultObjectMapper.print(request5));

            EmbeddingRequest request6 = EmbeddingRequest.builder()
                    .input(new String[]{"string1", "string2", "string3"})
                    .model("text-embedding-ada-002")
                    .build();

            System.out.println(DefaultObjectMapper.print(request6));

            Merlin.<OpenAiService>builder()
                    .addService(new OpenAiService())
                    .build()
                    .getService(OpenAiService.class)
                    .createEmbeddings(request1)
                    .join();
        }
    }

    @Nested
    class TestFineTuning {
        @Test
        void createFineTuning() {

        }

        @Test
        void listFineTuningJobs() {

        }

        @Test
        void listFineTuningEvents() {

        }

        @Test
        void listFineTuningCheckpoints() {

        }

        @Test
        void retrieveFineTuningJob() {

        }

        @Test
        void cancelFineTuningJob() {

        }
    }

    @Nested
    class TestBatch {

    }

    @Nested
    class TestAudio {
        @Test
        void createSpeech() {
            Merlin.<OpenAiService>builder()
                    .addService(new OpenAiService())
                    .build()
                    .getService(OpenAiService.class)
                    .createSpeech(SpeechRequest
                            .builder()
                            .model("tts-1")
                            .input("The quick brown fox jumped over the lazy dog.")
                            .voice("alloy")
                            .build())
                    .join();
        }
    }

    @Nested
    class TestFiles {
        @Test
        void uploadFile() {
            URL resourceUrl = OpenAiServiceTest.class.getClassLoader().getResource("log4j2.xml");
            assert resourceUrl != null;
            String filePath = Paths.get(resourceUrl.getPath()).toFile().getAbsolutePath();
            Merlin.<OpenAiService>builder()
                    .addService(new OpenAiService())
                    .build()
                    .getService(OpenAiService.class)
                    .uploadFile("fine-tune", filePath)
                    .join();
        }
    }


    @Nested
    class TestImages {

        @Test
        void createImage() {
            Merlin.<OpenAiService>builder()
                    .addService(new OpenAiService())
                    .build()
                    .getService(OpenAiService.class)
                    .createImage(ImageRequest.builder()
                            .prompt("A cute baby sea otter")
                            .n(1)
                            .build())
                    .join();
        }
    }

    @Nested
    class TestModels {

        @Test
        void testListModels() {
            Merlin.<OpenAiService>builder()
                    .addService(new OpenAiService())
                    .build()
                    .getService(OpenAiService.class)
                    .listModels()
                    .join();
        }

        @Test
        void testRetrieveModel() {
            Merlin.<OpenAiService>builder()
                    .addService(new OpenAiService())
                    .build()
                    .getService(OpenAiService.class)
                    .retrieveModel("gpt-3.5-turbo-instruct")
                    .join();
        }

        @Test
        void testDeleteAFineTunedModel() {
            Merlin.<OpenAiService>builder()
                    .addService(new OpenAiService())
                    .build()
                    .getService(OpenAiService.class)
                    .deleteAFineTunedModel("ft:gpt-3.5-turbo:acemeco:suffix:abc123")
                    .join();
        }
    }

    @Nested
    class ModerationTest {
        @Test
        void testCreateModeration() {
            Merlin.<OpenAiService>builder()
                    .addService(new OpenAiService())
                    .build()
                    .getService(OpenAiService.class)
                    .createModeration(ModerationRequest.builder()
                            .input("I want to kill them.")
                            .model("text-moderation-stable")
                            .build())
                    .join();
        }
    }

    @Nested
    class AssistantTest {

    }
}