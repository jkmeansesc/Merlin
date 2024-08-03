package org.merlin.service;

import org.merlin.annotations.UseRelay;
import org.merlin.annotations.UseWireMock;
import org.merlin.internal.constants.Provider;
import org.merlin.internal.utils.DefaultObjectMapper;
import org.merlin.model.gemini.GeminiData;
import org.merlin.model.gemini.caching.CachedContent;
import org.merlin.model.gemini.caching.Content;
import org.merlin.model.gemini.caching.Part;
import org.merlin.model.gemini.embeddings.BatchEmbedContentsRequest;
import org.merlin.model.gemini.embeddings.BatchEmbedContentsResponse;
import org.merlin.model.gemini.embeddings.EmbedContentRequest;
import org.merlin.model.gemini.embeddings.EmbedContentResponse;
import org.merlin.model.gemini.files.GeminiFile;
import org.merlin.model.gemini.files.UploadMediaRequest;
import org.merlin.model.gemini.files.UploadMediaResponse;
import org.merlin.model.gemini.generatingcontent.GenerateContentRequest;
import org.merlin.model.gemini.generatingcontent.GenerateContentResponse;
import org.merlin.model.gemini.models.Model;
import org.merlin.model.gemini.tokens.CountTokensRequest;
import org.merlin.model.gemini.tokens.CountTokensResponse;
import org.merlin.utils.TestHelper;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class GeminiServiceTest {
    private LlmConfig config;
    private GeminiService service;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        String baseUrl = GeminiService.DEFAULT_BASE_URL;
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
                baseUrl = "https://gemini.wiremockapi.cloud/";
            } else if (method.isAnnotationPresent(UseRelay.class)) {
                UseRelay annotation = method.getAnnotation(UseRelay.class);
                baseUrl = annotation.value();
                apiKey = p.getProperty("api.key");
            }
        }

        this.config = new LlmConfig(Provider.GOOGLE_GEMINI, baseUrl, apiKey);
        this.config.setTimeOut(Duration.ofSeconds(60));
        this.config.setLogLevel(LlmConfig.Level.BODY);
        this.service = Merlin.builder().gemini(config).build().getService(GeminiService.class);
    }

    @Test
    void test_all_constructors_can_be_initialized() {

        LlmService service_1 = new GeminiService();
        assertNotNull(service_1.getClient());
        assertNotNull(service_1.getMapper());
        assertNotNull(service_1.getRetrofit());
        assertNotNull(service_1.getConfig());

        this.config = new LlmConfig(Provider.GOOGLE_GEMINI, "https://test.baseUrl.com", "test token");
        LlmService service_2 = new GeminiService(config);
        assertEquals("https://test.baseUrl.com", service_2.getConfig().getBaseUrl(), "Wrong baseUrl");
        assertEquals("test token", service_2.getConfig().getToken(), "Wrong token");
    }

    @Nested
    class ModelsTest {
        @Test
        @UseWireMock
        void getModel() {
            String name = "models/gemini-1.5-pro";
            Model response = service.getModel(name).join();
            assertNotNull(response);
            assertEquals(name, response.getName());
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void listModels() {
            service.listModels(10, null).join();
            GeminiData<Model> response = service.listModels().join();
            assertNotNull(response.getFieldName());
            assertEquals("models", response.getFieldName());
            response.getData().forEach(model -> assertNotNull(model.getName()));
            System.out.println(DefaultObjectMapper.print(response));
        }
    }

    @Nested
    class GeneratingContentTest {

        @Test
        @UseWireMock
        void generateContent() {
            String model = "models/gemini-1.0-pro-latest";
            List<Part> parts = new ArrayList<>();
            parts.add(Part.builder().text("Write a story about a magic backpack.").build());
            List<Content> contents = new ArrayList<>();
            contents.add(Content.builder().parts(parts).role("user").build());
            GenerateContentRequest request = GenerateContentRequest.builder().contents(contents).build();
            System.out.println(DefaultObjectMapper.print(request));
            GenerateContentResponse response = service.generateContent(model, request).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @Disabled("Not mocked")
        void streamGenerateContent() {
            String model = "models/gemini-1.0-pro-latest";
            List<Part> parts = new ArrayList<>();
            parts.add(Part.builder().text("Write a story about a magic backpack.").build());
            List<Content> contents = new ArrayList<>();
            contents.add(Content.builder().parts(parts).role("user").build());
            GenerateContentRequest request = GenerateContentRequest.builder().contents(contents).build();
            System.out.println(DefaultObjectMapper.print(request));

            AtomicBoolean isDone = new AtomicBoolean(false);
            AtomicReference<Throwable> e = new AtomicReference<>();
            CompletableFuture<Void> future = new CompletableFuture<>();

            service.streamGenerateContent(model, request).start(chunk -> System.out.println("Received chunk: " + chunk), error -> {
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
    }

    @Nested
    class TokensTest {
        @Test
        @UseWireMock
        void countTokens() {
            String model = "models/gemini-1.0-pro-latest";
            List<Part> parts = new ArrayList<>();
            parts.add(Part.builder().text("Write a story about a magic backpack.").build());
            List<Content> contents = new ArrayList<>();
            contents.add(Content.builder().parts(parts).role("user").build());
            GenerateContentRequest r = GenerateContentRequest.builder().model(model).contents(contents).build();

            CountTokensRequest request = CountTokensRequest.builder().generateContentRequest(r).build();
            CountTokensResponse response = service.countTokens(model, request).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }
    }

    @Nested
    class FilesTest {

        @Test
        @UseWireMock
        void uploadMedia() throws IOException {
            java.io.File file = TestHelper.getFile("google_gemini/testFile.txt");
            String displayName = "TEXT";
            UploadMediaRequest request = UploadMediaRequest.builder().file(GeminiFile.builder().displayName(displayName).build()).build();

            UploadMediaResponse response = service.uploadMedia(request, file).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void uploadMediaMetadata() {
            String displayName = "Updated TEXT";
            UploadMediaRequest request = UploadMediaRequest.builder().file(GeminiFile.builder().displayName(displayName).build()).build();
            UploadMediaResponse response = service.uploadMediaMetadata(request).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void getFile() {
            String name = "files/af8g95pilnab";
            GeminiFile response = service.getFile(name).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void listFiles() {
            service.listFiles(20, null).join();
            GeminiData<GeminiFile> response = service.listFiles().join();
            assertNotNull(response);
            assertEquals("files", response.getFieldName());
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void deleteFile() {
            String name = "files/af8g95pilnab";
            try {
                service.deleteFile(name).join();
            } catch (Exception e) {
                fail("failed to delete file: " + e.getMessage());
            }
        }
    }

    @Nested
    class CachingTest {
        @Test
        @UseWireMock
        void createCachedContents() {
            String model = "models/gemini-1.5-flash-001";
            List<Part> parts = new ArrayList<>();
            parts.add(Part.builder().text("Write a story about a magic backpack.").build());
            List<Content> contents = new ArrayList<>();
            contents.add(Content.builder().parts(parts).role("user").build());
            CachedContent request = CachedContent.builder().model(model).contents(contents).build();

            CachedContent response = service.createCachedContent(request).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void listCachedContents() {
            service.listCachedContent(22, null).join();
            GeminiData<CachedContent> response = service.listCachedContent().join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void getCachedContent() {
            String name = "cachedContents/af8g95pilnab";
            CachedContent response = service.getCachedContent(name).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void updateCachedContent() {
            String name = "cachedContents/af8g95pilnab";
            List<Part> parts = new ArrayList<>();
            parts.add(Part.builder().text("Write a story about a magic backpack.").build());
            List<Content> contents = new ArrayList<>();
            contents.add(Content.builder().parts(parts).role("user").build());
            CachedContent request = CachedContent.builder().ttl("600s").contents(contents).build();

            service.updateCachedContent(name, request, null).join();
            CachedContent response = service.updateCachedContent(name, request).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void deleteCachedContent() {
            String name = "cachedContents/af8g95pilnab";
            try {
                service.deleteCachedContent(name).join();
            } catch (Exception e) {
                fail("failed to delete cached content: " + e.getMessage());
            }
        }
    }

    @Nested
    class EmbeddingsTest {

        @Test
        @UseWireMock
        void embedContent() {
            String model = "models/text-embedding-004";
            List<Part> parts = new ArrayList<>();
            parts.add(Part.builder().text("Hello World").build());
            EmbedContentRequest request = EmbedContentRequest
                    .builder()
                    .content(Content.builder().parts(parts).build())
                    .build();

            EmbedContentResponse response = service.embedContent(model, request).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }

        @Test
        @UseWireMock
        void batchEmbedContents() {
            String model = "models/text-embedding-004";

            List<Part> parts = new ArrayList<>();
            parts.add(Part.builder().text("Hello World").build());
            EmbedContentRequest r = EmbedContentRequest
                    .builder()
                    .model(model)
                    .content(Content.builder().parts(parts).build())
                    .build();
            List<EmbedContentRequest> requests = new ArrayList<>();
            requests.add(r);

            BatchEmbedContentsRequest request = BatchEmbedContentsRequest
                    .builder()
                    .requests(requests)
                    .build();
            BatchEmbedContentsResponse response = service.batchEmbedContents(model, request).join();
            assertNotNull(response);
            System.out.println(DefaultObjectMapper.print(response));
        }
    }
}