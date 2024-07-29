package org.haifan.merlin.service;

import okhttp3.ResponseBody;
import org.haifan.merlin.client.Merlin;
import org.haifan.merlin.internal.constants.Provider;
import org.haifan.merlin.internal.utils.DefaultObjectMapper;
import org.haifan.merlin.model.openai.*;
import org.haifan.merlin.model.openai.Function;
import org.haifan.merlin.model.openai.endpoints.audio.*;
import org.haifan.merlin.model.openai.endpoints.batch.Batch;
import org.haifan.merlin.model.openai.endpoints.batch.BatchRequest;
import org.haifan.merlin.model.openai.endpoints.chat.*;
import org.haifan.merlin.model.openai.endpoints.embeddings.Embedding;
import org.haifan.merlin.model.openai.endpoints.embeddings.EmbeddingRequest;
import org.haifan.merlin.model.openai.endpoints.files.OpenAiFile;
import org.haifan.merlin.model.openai.endpoints.finetune.FineTuningCheckpoint;
import org.haifan.merlin.model.openai.endpoints.finetune.FineTuningEvent;
import org.haifan.merlin.model.openai.endpoints.finetune.FineTuningJob;
import org.haifan.merlin.model.openai.endpoints.finetune.FineTuningJobRequest;
import org.haifan.merlin.model.openai.endpoints.images.Image;
import org.haifan.merlin.model.openai.endpoints.images.ImageRequest;
import org.haifan.merlin.model.openai.endpoints.moderations.ModerationRequest;
import org.haifan.merlin.utils.TestHelper;
import org.haifan.merlin.annotations.UseWireMock;
import org.haifan.merlin.annotations.UseRelay;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

class OpenAiServiceTest {

    private LlmConfig config;
    private OpenAiService service;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        String baseUrl = OpenAiService.DEFAULT_BASE_URL;
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
                baseUrl = "https://openai.wiremockapi.cloud/";
            } else if (method.isAnnotationPresent(UseRelay.class)) {
                UseRelay annotation = method.getAnnotation(UseRelay.class);
                baseUrl = annotation.value();
                apiKey = p.getProperty("api.key");
            }
        }

        this.config = new LlmConfig(Provider.OPENAI, baseUrl, apiKey);
        this.config.setTimeOut(Duration.ofSeconds(60));
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
    class AudioTest {
        @Test
        @UseRelay
        void createSpeech() throws IOException {
            ResponseBody response = Merlin.<OpenAiService>builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .createSpeech(SpeechRequest
                            .builder()
                            .model("tts-1")
                            .input("The quick brown fox jumped over the lazy dog.")
                            .voice("alloy")
                            .build())
                    .join();
            Path mp3 = TestHelper.audioToFile(response, Paths.get("src/test/resources/openai/output.mp3"));
            assertTrue(mp3.toFile().exists());
        }

        @Test
        @UseWireMock
        void createTranscription() {
            TranscriptionRequest request = TranscriptionRequest.builder()
                    .model("whisper-1")
                    .build();

            Transcription response = Merlin.<OpenAiService>builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .createTranscription(request, "src/test/resources/openai/output.mp3")
                    .join();
            assertNotNull(response.getText());
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void createTranslation() {
            TranslationRequest request = TranslationRequest.builder()
                    .model("whisper-1")
                    .prompt("translate into mandarine")
                    .build();

            Translation response = Merlin.<OpenAiService>builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .createTranslation(request, "src/test/resources/openai/output.mp3")
                    .join();

            assertNotNull(response.getText());
            System.out.println(DefaultObjectMapper.print(response));
        }
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
        @UseWireMock
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
        @UseRelay
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
                                messages.add(message); // not accumulated but small chunks
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
    class EmbeddingsTest {
        @Test
        @UseWireMock
        void createEmbeddings() {

            // Single string
            EmbeddingRequest request = EmbeddingRequest.builder()
                    .input("This is a test string")
                    .model("text-embedding-ada-002")
                    .encodingFormat("float")
                    .build();

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

            OpenAiData<Embedding> response = Merlin.<OpenAiService>builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .createEmbeddings(request)
                    .join();

            assertNotNull(response.getData(), "Data should not be null");
            assertTrue(response.getData().size() > 1, "Should receive at least one embedding");
        }
    }

    @Nested
    class FineTuningTest {
        @Test
        @UseWireMock
        void createFineTuningJob() {
            FineTuningJobRequest request = FineTuningJobRequest.builder()
                    .model("gpt-4o-mini")
                    .trainingFile("file-BK7bzQj3FfZFXr7DbL6xJwfo")
                    .build();

            FineTuningJob response = Merlin.builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .createFineTuningJob(request)
                    .join();
            assertNotNull(response.getStatus(), "Status should not be null");
            assertNotNull(response.getTrainingFile());
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        void listFineTuningJobs() {
            OpenAiData<FineTuningJob> response = Merlin.builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .listFineTuningJobs()
                    .join();

            assertEquals("list", response.getObject(), "object should be list");
            System.out.println(DefaultObjectMapper.print(response));

            OpenAiData<FineTuningJob> response_1 = Merlin.builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .listFineTuningJobs(null, 2)
                    .join();

            System.out.println(DefaultObjectMapper.print(response_1));
        }

        @Test
        @UseWireMock
        void listFineTuningEvents() {
            OpenAiData<FineTuningEvent> response = Merlin.builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .listFineTuningEvents("ftjob-abc123")
                    .join();
            assertEquals("list", response.getObject(), "object should be list");
            assertNotNull(response.getData(), "Data should not be null");

            System.out.println(DefaultObjectMapper.print(response));

            Merlin.builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .listFineTuningEvents("ftjob-abc123", "test", 2)
                    .join();
        }

        @Test
        @UseWireMock
        void listFineTuningCheckpoints() {
            OpenAiData<FineTuningCheckpoint> response = Merlin.builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .listFineTuningCheckpoints("ftjob-abc123")
                    .join();

            assertNotNull(response.getData(), "Data should not be null");
            System.out.println(DefaultObjectMapper.print(response));

            Merlin.builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .listFineTuningCheckpoints("ftjob-abc123", null, 2)
                    .join();
        }

        @Test
        @UseWireMock
        void retrieveFineTuningJob() {
            FineTuningJob response = Merlin.builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .retrieveFineTuningJob("ftjob-abc123")
                    .join();

            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void cancelFineTuningJob() {
            FineTuningJob response = Merlin.builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .cancelFineTuningJob("ftjob-abc123")
                    .join();
            assertNotNull(response);
            assertEquals("cancelled", response.getStatus(), "status should be cancelled");
            System.out.println(DefaultObjectMapper.print(response));
        }
    }

    @Nested
    class BatchTest {
        @Test
        @UseWireMock
        void createBatch() {

            String fileId = "file-abc123";
            String endpoint = "/v1/chat/completions";
            String completionWindow = "24h";

            BatchRequest request = BatchRequest.builder()
                    .inputFileId(fileId)
                    .endpoint(endpoint)
                    .completionWindow(completionWindow)
                    .build();

            Batch response = Merlin.builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .createBatch(request)
                    .join();

            assertEquals(fileId, response.getInputFileId(), "input file id mismatch");
            assertEquals(endpoint, response.getEndpoint(), "endpoint mismatch");
            assertEquals(completionWindow, response.getCompletionWindow(), "completion window mismatch");

            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void retrieveBatch() {
            String batchId = "batch_abc123";
            Batch response = Merlin.builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .retrieveBatch(batchId)
                    .join();
            assertEquals(batchId, response.getId(), "id mismatch");
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void cancelBatch() {
            String batchId = "batch_abc123";
            Batch response = Merlin.builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .cancelBatch(batchId)
                    .join();
            assertEquals(batchId, response.getId(), "id mismatch");
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void listBatches() {
            Merlin merlin = Merlin.builder().addService(service).build();
            merlin.getService(OpenAiService.class)
                    .listBatches()
                    .join();
            OpenAiData<Batch> response = merlin.getService(OpenAiService.class)
                    .listBatches(null, 2)
                    .join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }
    }

    @Nested
    class FilesTest {

        @Test
        @UseWireMock
        void uploadFile() {
            URL resourceUrl = OpenAiServiceTest.class.getClassLoader().getResource("log4j2.xml");
            assert resourceUrl != null;
            String filePath = Paths.get(resourceUrl.getPath()).toFile().getAbsolutePath();
            OpenAiFile response = Merlin.<OpenAiService>builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .uploadFile("fine-tune", filePath)
                    .join();
            assertEquals("log4j2.xml", response.getFilename(), "filename mismatch");
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void listFiles() {
            OpenAiData<OpenAiFile> response = Merlin.builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .listFiles()
                    .join();
            assertNotNull(response.getData());
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void retrieveFile() {
            String fileId = "file-EGQhIfguFLF0hHfCu0iVF9xL";
            OpenAiFile response = Merlin.<OpenAiService>builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .retrieveFile(fileId)
                    .join();
            assertEquals("log4j2.xml", response.getFilename(), "filename mismatch");
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        void deleteFile() {
            String fileId = "file-EGQhIfguFLF0hHfCu0iVF9xL";
            DeletionStatus response = Merlin.<OpenAiService>builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .deleteFile(fileId)
                    .join();
            assertTrue(response.isDeleted(), "deleted field should indicate true");
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void retrieveFileContent() throws IOException {
            String fileId = "file-EGQhIfguFLF0hHfCu0iVF9xL";
            ResponseBody response = Merlin.<OpenAiService>builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .retrieveFileContent(fileId)
                    .join();
            assertNotNull(response);
            Path relativePath = Path.of("openai", "log4j2.xml");
            Path filePath = TestHelper.writeResponseBodyToFile(response, relativePath);
            assertTrue(Files.exists(filePath));
        }
    }

    @Nested
    class ImagesTest {
        @Test
        @UseWireMock
        void createImage() {
            ImageRequest request = ImageRequest.builder()
                    .model("dall-e-3")
                    .prompt("A cute baby sea otter")
                    .n(1)
                    .size("1024x1024")
                    .build();
            OpenAiData<Image> response = Merlin.builder()
                    .addService(service)
                    .build()
                    .getService(OpenAiService.class)
                    .createImage(request)
                    .join();
            assertNotNull(response);
            assertFalse(response.getData().isEmpty());
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        void createImageEdit() {
        }

        @Test
        void createImageVariation() {
        }
    }

    @Nested
    class ModelsTest {
        @Test
        void listModels() {
        }

        @Test
        void retrieveModel() {
        }

        @Test
        void deleteAFineTunedModel() {
        }
    }


    @Nested
    class ModerationTest {
        @Test
        void createModeration() {
            Merlin.<OpenAiService>builder()
                    .addService(service)
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
    class AssistantsTest {
        @Test
        void createAssistant() {
        }

        @Test
        void listAssistants() {
        }

        @Test
        void retrieveAssistant() {
        }

        @Test
        void modifyAssistant() {
        }

        @Test
        void deleteAssistant() {
        }
    }

    @Nested
    class ThreadsTest {
        @Test
        void createThread() {
        }

        @Test
        void retrieveThread() {
        }

        @Test
        void modifyThread() {
        }

        @Test
        void deleteThread() {
        }
    }

    @Nested
    class MessagesTest {
        @Test
        void createMessage() {
        }

        @Test
        void listMessages() {
        }

        @Test
        void retrieveMessage() {
        }

        @Test
        void modifyMessage() {
        }

        @Test
        void deleteMessage() {
        }
    }

    @Nested
    class RunsTest {
        @Test
        void createRun() {
        }

        @Test
        void createThreadAndRun() {
        }

        @Test
        void listRuns() {
        }

        @Test
        void retrieveRun() {
        }

        @Test
        void modifyRun() {
        }

        @Test
        void submitToolOutputsToRun() {
        }

        @Test
        void cancelRun() {
        }
    }

    @Nested
    class RunStepsTest {
        @Test
        void listRunSteps() {
        }

        @Test
        void retrieveRunStep() {
        }
    }

    @Nested
    class VectorStoreTest {
        @Test
        void createVectorStore() {
        }

        @Test
        void listVectorStores() {
        }

        @Test
        void retrieveVectorStore() {
        }

        @Test
        void modifyVectorStore() {
        }

        @Test
        void deleteVectorStore() {
        }
    }

    @Nested
    class VectorStoreFileTest {
        @Test
        void createVectorStoreFile() {
        }

        @Test
        void listVectorStoreFiles() {
        }

        @Test
        void retrieveVectorStoreFile() {
        }

        @Test
        void deleteVectorStoreFile() {
        }
    }

    @Nested
    class VectorStoreFileBatchTest {

        @Test
        void createVectorStoreFileBatch() {
        }

        @Test
        void retrieveVectorStoreFileBatch() {
        }

        @Test
        void cancelVectorStoreFileBatch() {
        }

        @Test
        void listVectorStoreFileBatch() {
        }
    }
}