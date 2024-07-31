package org.haifan.merlin.service;

import okhttp3.ResponseBody;
import org.haifan.merlin.annotations.UseRelay;
import org.haifan.merlin.annotations.UseWireMock;
import org.haifan.merlin.internal.constants.Provider;
import org.haifan.merlin.internal.utils.DefaultObjectMapper;
import org.haifan.merlin.model.openai.*;
import org.haifan.merlin.model.openai.assistants.assistants.Assistant;
import org.haifan.merlin.model.openai.assistants.assistants.AssistantRequest;
import org.haifan.merlin.model.openai.assistants.messages.MessageObject;
import org.haifan.merlin.model.openai.assistants.messages.MessageRequest;
import org.haifan.merlin.model.openai.assistants.runs.Run;
import org.haifan.merlin.model.openai.assistants.runs.RunRequest;
import org.haifan.merlin.model.openai.assistants.runs.RunStep;
import org.haifan.merlin.model.openai.assistants.runs.ToolOutputRequest;
import org.haifan.merlin.model.openai.assistants.threads.OpenAiThread;
import org.haifan.merlin.model.openai.assistants.threads.ThreadRequest;
import org.haifan.merlin.model.openai.assistants.vectorstores.*;
import org.haifan.merlin.model.openai.endpoints.audio.*;
import org.haifan.merlin.model.openai.endpoints.batch.Batch;
import org.haifan.merlin.model.openai.endpoints.batch.BatchRequest;
import org.haifan.merlin.model.openai.endpoints.chat.ChatCompletion;
import org.haifan.merlin.model.openai.endpoints.chat.ChatCompletionRequest;
import org.haifan.merlin.model.openai.endpoints.embeddings.Embedding;
import org.haifan.merlin.model.openai.endpoints.embeddings.EmbeddingRequest;
import org.haifan.merlin.model.openai.endpoints.files.OpenAiFile;
import org.haifan.merlin.model.openai.endpoints.finetune.FineTuningCheckpoint;
import org.haifan.merlin.model.openai.endpoints.finetune.FineTuningEvent;
import org.haifan.merlin.model.openai.endpoints.finetune.FineTuningJob;
import org.haifan.merlin.model.openai.endpoints.finetune.FineTuningJobRequest;
import org.haifan.merlin.model.openai.endpoints.images.Image;
import org.haifan.merlin.model.openai.endpoints.images.ImageEditRequest;
import org.haifan.merlin.model.openai.endpoints.images.ImageRequest;
import org.haifan.merlin.model.openai.endpoints.images.ImageVariationRequest;
import org.haifan.merlin.model.openai.endpoints.models.Model;
import org.haifan.merlin.model.openai.endpoints.moderations.ModerationList;
import org.haifan.merlin.model.openai.endpoints.moderations.ModerationRequest;
import org.haifan.merlin.utils.TestHelper;
import org.junit.jupiter.api.*;

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

import static org.junit.jupiter.api.Assertions.*;

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
        this.service = Merlin.builder().openai(config).build().getService(OpenAiService.class);
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
            ResponseBody response = service.createSpeech(SpeechRequest.builder().model("tts-1").input("The quick brown fox jumped over the lazy dog.").voice("alloy").build()).join();
            Path mp3 = TestHelper.audioToFile(response, Paths.get("src/test/resources/openai/output.mp3"));
            assertTrue(mp3.toFile().exists());
        }

        @Test
        @UseWireMock
        void createTranscription() {
            TranscriptionRequest request = TranscriptionRequest.builder().model("whisper-1").build();

            Transcription response = service.createTranscription(request, "src/test/resources/openai/output.mp3").join();
            assertNotNull(response.getText());
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void createTranslation() {
            TranslationRequest request = TranslationRequest.builder().model("whisper-1").prompt("translate into mandarine").temperature(0.5).build();

            Translation response = service.createTranslation(request, "src/test/resources/openai/output.mp3").join();

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
            tools.add(new Tool("auto", new Function()));

            Map<String, Integer> logitBias = new HashMap<>();
            logitBias.put("logitBias", 1);

            ChatCompletionRequest request = ChatCompletionRequest.builder().messages(messages).model("model").frequencyPenalty(0.1).logitBias(logitBias).logprobs(false).topLogprobs(1).maxTokens(400).n(5).presencePenalty(1.1).responseFormat(new ResponseFormat("type")).seed(1).serviceTier("auto").stop(Arrays.stream(new String[]{"1", "2"}).toList()).stream(false).streamOptions(new ChatCompletionRequest.StreamOptions()).temperature(11.0).topP(10.1).tools(tools).toolChoice(new ToolChoice("auto")).parallelToolCalls(false).user("user").build();

            assertEquals("model", request.getModel());
            System.out.println(DefaultObjectMapper.print(request));
        }

        @Test
        @UseWireMock
        void createChatCompletion() {
            List<OpenAiMessage> messages = new ArrayList<>();
            messages.add(new OpenAiMessage(OpenAiMessage.Role.USER, "Are you there?"));

            ChatCompletionRequest request = ChatCompletionRequest.builder().model("gpt-4o").messages(messages).build();
            ChatCompletion response = service.createChatCompletion(request).join();

            assertNotNull(response.getChoices().get(0).getMessage());
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseRelay
        @Disabled("Not mocked")
        void streamChatCompletion() {
            List<OpenAiMessage> messages = new ArrayList<>();
            messages.add(new OpenAiMessage(OpenAiMessage.Role.USER, "Hi, are you there?"));

            ChatCompletionRequest request = ChatCompletionRequest.builder().model("gpt-3.5-turbo-0125").messages(messages).build();

            AtomicBoolean isDone = new AtomicBoolean(false);
            AtomicReference<Throwable> e = new AtomicReference<>();
            CompletableFuture<Void> future = new CompletableFuture<>();

            service.streamChatCompletion(request).start(chunk -> {
                System.out.println("Received chunk: " + chunk);
                OpenAiMessage message = chunk.getChoices().get(0).getMessage();
                messages.add(message); // not accumulated but small chunks
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
            EmbeddingRequest request = EmbeddingRequest.builder().input("This is a test string").model("text-embedding-ada-002").encodingFormat("float").build();

            // Array of strings
            List<String> stringList = Arrays.asList("String 1", "String 2", "String 3");
            EmbeddingRequest request2 = EmbeddingRequest.builder().input(stringList).model("text-embedding-ada-002").build();

            System.out.println(DefaultObjectMapper.print(request2));

            // Array of integers
            List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5);
            EmbeddingRequest request3 = EmbeddingRequest.builder().input(intList).model("text-embedding-ada-002").build();

            System.out.println(DefaultObjectMapper.print(request3));

            // Array of arrays of integers
            List<List<Integer>> nestedList = Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6), Arrays.asList(7, 8, 9));

            EmbeddingRequest request4 = EmbeddingRequest.builder().input(nestedList).model("text-embedding-ada-002").build();

            System.out.println(DefaultObjectMapper.print(request4));

            EmbeddingRequest request5 = EmbeddingRequest.builder().input(new Integer[]{1, 2, 3}).model("text-embedding-ada-002").build();

            System.out.println(DefaultObjectMapper.print(request5));

            EmbeddingRequest request6 = EmbeddingRequest.builder().input(new String[]{"string1", "string2", "string3"}).model("text-embedding-ada-002").build();

            System.out.println(DefaultObjectMapper.print(request6));

            OpenAiData<Embedding> response = service.createEmbeddings(request).join();

            assertNotNull(response.getData(), "Data should not be null");
            assertFalse(response.getData().isEmpty(), "Should receive at least one embedding");
        }
    }

    @Nested
    class FineTuningTest {
        @Test
        @UseWireMock
        void createFineTuningJob() {
            FineTuningJobRequest request = FineTuningJobRequest.builder().model("gpt-4o-mini").trainingFile("file-BK7bzQj3FfZFXr7DbL6xJwfo").build();

            FineTuningJob response = service.createFineTuningJob(request).join();
            assertNotNull(response.getStatus(), "Status should not be null");
            assertNotNull(response.getTrainingFile());
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        void listFineTuningJobs() {
            OpenAiData<FineTuningJob> response = service.listFineTuningJobs().join();

            assertEquals("list", response.getObject(), "object should be list");
            System.out.println(DefaultObjectMapper.print(response));

            OpenAiData<FineTuningJob> response_1 = service.listFineTuningJobs(null, 2).join();

            System.out.println(DefaultObjectMapper.print(response_1));
        }

        @Test
        @UseWireMock
        void listFineTuningEvents() {
            OpenAiData<FineTuningEvent> response = service.listFineTuningEvents("ftjob-abc123").join();
            assertEquals("list", response.getObject(), "object should be list");
            assertNotNull(response.getData(), "Data should not be null");

            System.out.println(DefaultObjectMapper.print(response));

            service.listFineTuningEvents("ftjob-abc123", "test", 2).join();
        }

        @Test
        @UseWireMock
        void listFineTuningCheckpoints() {
            OpenAiData<FineTuningCheckpoint> response = service.listFineTuningCheckpoints("ftjob-abc123").join();

            assertNotNull(response.getData(), "Data should not be null");
            System.out.println(DefaultObjectMapper.print(response));

            service.listFineTuningCheckpoints("ftjob-abc123", null, 2).join();
        }

        @Test
        @UseWireMock
        void retrieveFineTuningJob() {
            FineTuningJob response = service.retrieveFineTuningJob("ftjob-abc123").join();

            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void cancelFineTuningJob() {
            FineTuningJob response = service.cancelFineTuningJob("ftjob-abc123").join();
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

            BatchRequest request = BatchRequest.builder().inputFileId(fileId).endpoint(endpoint).completionWindow(completionWindow).build();

            Batch response = service.createBatch(request).join();

            assertEquals(fileId, response.getInputFileId(), "input file id mismatch");
            assertEquals(endpoint, response.getEndpoint(), "endpoint mismatch");
            assertEquals(completionWindow, response.getCompletionWindow(), "completion window mismatch");

            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void retrieveBatch() {
            String batchId = "batch_abc123";
            Batch response = service.retrieveBatch(batchId).join();
            assertEquals(batchId, response.getId(), "id mismatch");
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void cancelBatch() {
            String batchId = "batch_abc123";
            Batch response = service.cancelBatch(batchId).join();
            assertEquals(batchId, response.getId(), "id mismatch");
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void listBatches() {
            service.listBatches().join();
            OpenAiData<Batch> response = service.listBatches(null, 2).join();
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
            OpenAiFile response = service.uploadFile("fine-tune", filePath).join();
            assertEquals("log4j2.xml", response.getFilename(), "filename mismatch");
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void listFiles() {
            OpenAiData<OpenAiFile> response = service.listFiles().join();
            assertNotNull(response.getData());
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void retrieveFile() {
            String fileId = "file-EGQhIfguFLF0hHfCu0iVF9xL";
            OpenAiFile response = service.retrieveFile(fileId).join();
            assertEquals("log4j2.xml", response.getFilename(), "filename mismatch");
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void deleteFile() {
            String fileId = "file-EGQhIfguFLF0hHfCu0iVF9xL";
            DeletionStatus response = service.deleteFile(fileId).join();
            assertTrue(response.isDeleted(), "deleted field should indicate true");
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void retrieveFileContent() throws IOException {
            String fileId = "file-EGQhIfguFLF0hHfCu0iVF9xL";
            ResponseBody response = service.retrieveFileContent(fileId).join();
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
            ImageRequest request = ImageRequest.builder().model("dall-e-3").prompt("A cute baby sea otter").n(1).size("1024x1024").build();
            OpenAiData<Image> response = service.createImage(request).join();
            assertNotNull(response);
            assertFalse(response.getData().isEmpty());
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void createImageEdit() {
            ImageEditRequest request = ImageEditRequest.builder().prompt("A cute baby sea otter wearing a beret").build();
            OpenAiData<Image> response = service.createImageEdit(request, "src/test/resources/openai/img-HFqmDa3RTJ09YQm2mHxmjNbq.png", null).join();
            assertNotNull(response);
            assertFalse(response.getData().isEmpty());
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void createImageVariation() {
            ImageVariationRequest request = ImageVariationRequest.builder().model("dall-e-2").n(2).size("1024x1024").responseFormat("url").user("me").build();
            OpenAiData<Image> response = service.createImageVariation(request, "src/test/resources/openai/img-HFqmDa3RTJ09YQm2mHxmjNbq.png").join();

            assertNotNull(response);
            assertFalse(response.getData().isEmpty());
            System.out.println(DefaultObjectMapper.print(response));
        }
    }

    @Nested
    class ModelsTest {
        @Test
        @UseWireMock
        void listModels() {
            OpenAiData<Model> response = service.listModels().join();

            assertNotNull(response);
            assertFalse(response.getData().isEmpty());
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void retrieveModel() {
            String model = "gpt-3.5-turbo-instruct";
            Model response = service.retrieveModel(model).join();
            assertNotNull(response);
            assertEquals(model, response.getId());
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void deleteAFineTunedModel() {
            String model = "ft:gpt-4o-mini:acemeco:suffix:abc123";
            DeletionStatus response = service.deleteAFineTunedModel(model).join();
            assertNotNull(response);
            assertEquals(model, response.getId());
            assertTrue(response.isDeleted(), "deleted field should indicate true");
        }
    }

    @Nested
    class ModerationTest {
        @Test
        @UseWireMock
        void createModeration() {
            ModerationList response = service.createModeration(ModerationRequest.builder().input("I want to kill them.").model("text-moderation-stable").build()).join();
            assertNotNull(response);
            assertFalse(response.getResults().isEmpty());
        }
    }

    @Nested
    class AssistantsTest {
        @Test
        @UseWireMock
        void createAssistant() {
            List<AssistantTool> tools = new ArrayList<>();
            tools.add(new CodeInterpreterTool());

            String model = "gpt-4o";
            String name = "Math Tutor";

            AssistantRequest request = AssistantRequest.builder().model(model).name(name).tools(tools).instructions("You are a personal math tutor. When asked a question, write and run Python code to answer the question.").build();
            Assistant response = service.createAssistant(request).join();
            assertNotNull(response);
            assertEquals(model, response.getModel(), "model mismatch");
            assertEquals(name, response.getName(), "name mismatch");
            assertEquals("code_interpreter", response.getTools().get(0).getType(), "tools mismatch");
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void listAssistants() {
            Map<String, String> query = new HashMap<>();
            query.put("limit", "2");
            query.put("order", "asc");
            service.listAssistants(query).join();
            OpenAiData<Assistant> response = service.listAssistants().join();
            assertNotNull(response);
            assertNotNull(response.getData());
            assertEquals("list", response.getObject(), "data should be list");
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void retrieveAssistant() {
            String assistantId = "asst_abc123";
            Assistant response = service.retrieveAssistant(assistantId).join();
            assertNotNull(response);
            assertEquals(assistantId, response.getId(), "id mismatch");
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void modifyAssistant() {
            String assistantId = "asst_abc123";
            String model = "gpt-4o";
            String instructions = "You are an HR bot, and you have access to files to answer employee questions about company policies. Always response with info from either of the files.";
            List<AssistantTool> tools = new ArrayList<>();
            tools.add(new FileSearchTool());

            AssistantRequest request = AssistantRequest.builder().model(model).tools(tools).instructions(instructions).tools(tools).build();
            Assistant response = service.modifyAssistant(assistantId, request).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void deleteAssistant() {
            String assistantId = "asst_abc123";
            DeletionStatus response = service.deleteAssistant(assistantId).join();
            assertNotNull(response);
            assertTrue(response.isDeleted(), "deleted field should indicate true");
            System.out.println(DefaultObjectMapper.print(response));
        }
    }

    @Nested
    class ThreadsTest {
        @Test
        @UseWireMock
        void createThread() {

            MessageRequest userMessage = MessageRequest.builder().role("user").content(new Content("content")).build();

            List<ContentPart> parts = new ArrayList<>();
            parts.add(ImageFileContentPart.builder().imageFile(ImageFile.builder().fileId("image file id").detail("image file detail").build()).build());
            parts.add(ImageUrlContentPart.builder().imageUrl(ImageUrl.builder().url("https://example.image.url.com/example.png").detail("image url detail").build()).build());
            List<Annotation> annotations = new ArrayList<>();
            annotations.add(Annotation.builder().build());
            parts.add(TextContentPart.builder().text(Text.builder().value("text content part").annotations(annotations).build()).build());

            MessageRequest assistantMessage = MessageRequest.builder().role("assistant").content(new Content(parts)).build();

            List<MessageRequest> messages = new ArrayList<>();
            messages.add(userMessage);
            messages.add(assistantMessage);

            ToolResources toolResources = new ToolResources();
            ToolResources.CodeInterpreter codeInterpreter = new ToolResources.CodeInterpreter();
            List<String> fileIds = new ArrayList<>();
            fileIds.add("fileId_1");
            fileIds.add("fileId_2");
            fileIds.add("fileId_3");
            codeInterpreter.setFileIds(fileIds);

            ToolResources.FileSearch fileSearch = new ToolResources.FileSearch();
            fileSearch.setVectorStoreIds(List.of("id_1"));

            ToolResources.FileSearch.VectorStoreHelper vectorStoreHelper = new ToolResources.FileSearch.VectorStoreHelper();
            vectorStoreHelper.setFileIds(List.of("fileId_1"));
            vectorStoreHelper.setChunkingStrategy(new AutoChunkingStrategy());
            vectorStoreHelper.setMetadata(new HashMap<>());
            List<ToolResources.FileSearch.VectorStoreHelper> vectorStoreHelpers = new ArrayList<>();
            vectorStoreHelpers.add(vectorStoreHelper);
            fileSearch.setVectorStores(vectorStoreHelpers);

            toolResources.setCodeInterpreter(codeInterpreter);
            toolResources.setFileSearch(fileSearch);

            ThreadRequest request = ThreadRequest.builder().messages(messages).toolResources(toolResources).metadata(new HashMap<>()).build();

            OpenAiThread response = service.createThread(request).join();
            assertNotNull(response);
            assertEquals("thread", response.getObject(), "object should be thread");
            System.out.println(DefaultObjectMapper.print(response));
            System.out.println(DefaultObjectMapper.print(request));
        }

        @Test
        @UseWireMock
        void retrieveThread() {
            String threadId = "thread_abc123";
            OpenAiThread response = service.retrieveThread(threadId).join();
            assertNotNull(response);
            assertEquals(threadId, response.getId(), "id mismatch");
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void modifyThread() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("modified", true);
            metadata.put("user", "abc123");

            String threadId = "thread_abc123";
            ThreadRequest request = ThreadRequest.builder().metadata(metadata).build();

            OpenAiThread response = service.modifyThread(threadId, request).join();
            assertNotNull(response);
            assertEquals(threadId, response.getId(), "id mismatch");
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void deleteThread() {
            String threadId = "thread_abc123";
            DeletionStatus response = service.deleteThread(threadId).join();
            assertNotNull(response);
            assertTrue(response.isDeleted(), "deleted field should indicate true");
            System.out.println(DefaultObjectMapper.print(response));
        }
    }

    @Nested
    class MessagesTest {
        @Test
        @UseWireMock
        void createMessage() {
            String threadId = "thread_abc123";
            MessageRequest request = MessageRequest.builder().role("user").content(new Content("content")).build();
            MessageObject response = service.createMessage(threadId, request).join();
            assertNotNull(response);
            assertEquals(threadId, response.getThreadId(), "thread id mismatch");
            System.out.println(DefaultObjectMapper.print(response));

            for (ContentPart part : response.getContent()) {
                assertNotNull(part.getType());
                System.out.println("ContentPart type: " + part.getType());
            }
        }

        @Test
        @UseWireMock
        void listMessages() {
            String threadId = "thread_abc123";
            Map<String, String> query = new HashMap<>();
            query.put("limit", "2");
            query.put("order", "asc");
            service.listMessages(threadId, query).join();
            OpenAiData<MessageObject> response = service.listMessages(threadId).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void retrieveMessage() {
            String threadId = "thread_abc123";
            String messageId = "msg_abc123";
            MessageObject response = service.retrieveMessage(threadId, messageId).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void modifyMessage() {
            String threadId = "thread_abc123";
            String messageId = "msg_abc123";
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("modified", true);
            metadata.put("user", "abc123");
            MessageRequest request = MessageRequest.builder().metadata(metadata).build();
            MessageObject response = service.modifyMessage(threadId, messageId, request).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void deleteMessage() {
            String threadId = "thread_abc123";
            String messageId = "msg_abc123";
            DeletionStatus response = service.deleteMessage(threadId, messageId).join();
            assertNotNull(response);
            assertTrue(response.isDeleted(), "deleted field should indicate true");
            System.out.println(DefaultObjectMapper.print(response));
        }
    }

    @Nested
    class RunsTest {
        @Test
        @UseWireMock
        void createRun() {
            String threadId = "thread_abc123";
            String assistantId = "assistant_abc123";
            RunRequest request = RunRequest.builder().assistantId(assistantId).build();
            Run response = service.createRun(threadId, request).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void createThreadAndRun() {
            String assistantId = "assistant_abc123";
            RunRequest request = RunRequest.builder().assistantId(assistantId).build();
            Run response = service.createThreadAndRun(request).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void listRuns() {
            String threadId = "thread_abc123";
            Map<String, String> query = new HashMap<>();
            query.put("limit", "2");
            service.listRuns(threadId, query).join();
            OpenAiData<Run> response = service.listRuns(threadId).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void retrieveRun() {
            String threadId = "thread_abc123";
            String runId = "run_abc123";
            Run response = service.retrieveRun(threadId, runId).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void modifyRun() {
            String threadId = "thread_abc123";
            String runId = "run_abc123";

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("modified", true);
            metadata.put("user", "abc123");

            RunRequest request = RunRequest.builder().metadata(metadata).build();
            Run response = service.modifyRun(threadId, runId, request).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void submitToolOutputsToRun() {
            String threadId = "thread_abc123";
            String runId = "run_abc123";
            List<ToolOutput> toolOutputs = new ArrayList<>();
            toolOutputs.add(ToolOutput.builder().toolCallId("abc123").output("output").build());
            ToolOutputRequest request = ToolOutputRequest.builder().toolOutputs(toolOutputs).build();
            Run response = service.submitToolOutputsToRun(threadId, runId, request).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @Disabled("Not mocked")
        void streamToolOutputsToRun() {
            String threadId = "thread_abc123";
            String runId = "run_abc123";
            List<ToolOutput> toolOutputs = new ArrayList<>();
            toolOutputs.add(ToolOutput.builder().toolCallId("abc123").output("output").build());
            ToolOutputRequest request = ToolOutputRequest.builder().toolOutputs(toolOutputs).build();

            AtomicBoolean isDone = new AtomicBoolean(false);
            AtomicReference<Throwable> e = new AtomicReference<>();
            CompletableFuture<Void> future = new CompletableFuture<>();

            service.streamToolOutputsToRun(threadId, runId, request)
                    .start(run -> {
                        System.out.println("Received chunk: " + run);
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
        }

        @Test
        @UseWireMock
        void cancelRun() {
            String threadId = "thread_abc123";
            String runId = "run_abc123";
            Run response = service.cancelRun(threadId, runId).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }
    }

    @Nested
    class RunStepsTest {
        @Test
        @UseWireMock
        void listRunSteps() {
            String threadId = "thread_abc123";
            String runId = "run_abc123";
            Map<String, String> query = new HashMap<>();
            query.put("limit", "2");
            service.listRunSteps(threadId, runId, query).join();
            OpenAiData<RunStep> response = service.listRunSteps(threadId, runId).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void retrieveRunStep() {
            String threadId = "thread_abc123";
            String runId = "run_abc123";
            String stepId = "step_abc123";
            RunStep response = service.retrieveRunStep(threadId, runId, stepId).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }
    }

    @Nested
    class VectorStoreTest {

        @Test
        @UseWireMock
        void createVectorStore() {
            String name = "Support FAQ";
            VectorStoreRequest request = VectorStoreRequest.builder().name(name).build();
            VectorStore response = service.createVectorStore(request).join();
            assertNotNull(response);
            assertEquals(name, response.getName());
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void listVectorStores() {
            Map<String, String> query = new HashMap<>();
            query.put("limit", "2");
            service.listVectorStores(query).join();
            OpenAiData<VectorStore> response = service.listVectorStores().join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void retrieveVectorStore() {
            String vectorId = "vs_3kMYHJNc5til8L7mc2z4m16X";
            VectorStore response = service.retrieveVectorStore(vectorId).join();
            assertNotNull(response);
            assertEquals(vectorId, response.getId(), "Vector id should be the same");
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void modifyVectorStore() {
            String vectorId = "vs_3kMYHJNc5til8L7mc2z4m16X";
            String name = "Support FAQ";
            VectorStoreRequest request = VectorStoreRequest.builder().name(name).build();
            VectorStore response = service.modifyVectorStore(vectorId, request).join();
            assertNotNull(response);
            assertEquals(name, response.getName());
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void deleteVectorStore() {
            String vectorId = "vs_abc123";
            DeletionStatus response = service.deleteVectorStore(vectorId).join();
            assertNotNull(response);
            assertTrue(response.isDeleted(), "deleted should indicate true");
            System.out.println(DefaultObjectMapper.print(response));
        }
    }

    @Nested
    class VectorStoreFileTest {
        @Test
        @UseWireMock
        void createVectorStoreFile() {
            String fileId = "file-abc123";
            String vectorStoreId = "vs_abc123";
            VectorStoreFileRequest request = VectorStoreFileRequest.builder().fileId(fileId).build();
            VectorStoreFile response = service.createVectorStoreFile(vectorStoreId, request).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void listVectorStoreFiles() {
            String vectorStoreId = "vs_abc123";
            OpenAiData<VectorStoreFile> response = service.listVectorStoreFiles(vectorStoreId).join();
            Map<String, String> query = new HashMap<>();
            query.put("order", "asc");
            service.listVectorStoreFiles(vectorStoreId, query).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void retrieveVectorStoreFile() {
            String fileId = "file-abc123";
            String vectorStoreId = "vs_abc123";
            VectorStoreFile response = service.retrieveVectorStoreFile(vectorStoreId, fileId).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void deleteVectorStoreFile() {
            String fileId = "file-abc123";
            String vectorStoreId = "vs_abc123";
            DeletionStatus response = service.deleteVectorStoreFile(vectorStoreId, fileId).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }
    }

    @Nested
    class VectorStoreFileBatchTest {

        @Test
        @UseWireMock
        void createVectorStoreFileBatch() {
            String vectorStoreId = "vs_abc123";
            VectorStoreFileBatchRequest request = VectorStoreFileBatchRequest
                    .builder()
                    .fileIds(Arrays.asList("file_id_1", "file_id_2", "file_id_3", "file_id_4"))
                    .build();
            VectorStoreFileBatch response = service.createVectorStoreFileBatch(vectorStoreId, request).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void retrieveVectorStoreFileBatch() {
            String vectorStoreId = "vs_abc123";
            String batchId = "batch_abc123";
            VectorStoreFileBatch response = service.retrieveVectorStoreFileBatch(vectorStoreId, batchId).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void cancelVectorStoreFileBatch() {
            String vectorStoreId = "vs_abc123";
            String batchId = "batch_abc123";
            VectorStoreFileBatch resposne = service.cancelVectorStoreFileBatch(vectorStoreId, batchId).join();
            assertNotNull(resposne);
            System.out.println(DefaultObjectMapper.print(resposne));
        }

        @Test
        @UseWireMock
        void listVectorStoreFileBatch() {
            String vectorStoreId = "vs_abc123";
            String batchId = "batch_abc123";
            Map<String, String> query = new HashMap<>();
            query.put("limit", "2");
            service.listVectorStoreFileBatch(vectorStoreId, batchId, query).join();
            OpenAiData<VectorStoreFileBatch> response = service.listVectorStoreFileBatch(vectorStoreId, batchId).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }
    }
}