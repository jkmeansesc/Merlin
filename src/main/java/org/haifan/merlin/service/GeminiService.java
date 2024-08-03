package org.haifan.merlin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.haifan.merlin.internal.api.GeminiApi;
import org.haifan.merlin.internal.constants.Provider;
import org.haifan.merlin.internal.interceptors.GeminiInterceptor;
import org.haifan.merlin.internal.utils.DefaultObjectMapper;
import org.haifan.merlin.internal.utils.FileParser;
import org.haifan.merlin.model.StreamingResponse;
import org.haifan.merlin.model.gemini.*;
import org.haifan.merlin.model.gemini.caching.CachedContent;
import org.haifan.merlin.model.gemini.embeddings.BatchEmbedContentsRequest;
import org.haifan.merlin.model.gemini.embeddings.BatchEmbedContentsResponse;
import org.haifan.merlin.model.gemini.embeddings.EmbedContentRequest;
import org.haifan.merlin.model.gemini.embeddings.EmbedContentResponse;
import org.haifan.merlin.model.gemini.files.GeminiFile;
import org.haifan.merlin.model.gemini.files.UploadMediaRequest;
import org.haifan.merlin.model.gemini.files.UploadMediaResponse;
import org.haifan.merlin.model.gemini.generatingcontent.GenerateContentRequest;
import org.haifan.merlin.model.gemini.generatingcontent.GenerateContentResponse;
import org.haifan.merlin.model.gemini.models.Model;
import org.haifan.merlin.model.gemini.tokens.CountTokensRequest;
import org.haifan.merlin.model.gemini.tokens.CountTokensResponse;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Service implementation for interacting with the Gemini Language Model API.
 * Extends the base LlmService to provide methods for model management and content generation.
 */
public class GeminiService extends LlmService {

    private final GeminiApi api;
    public static final String DEFAULT_BASE_URL = "https://generativelanguage.googleapis.com";

    /**
     * Constructs a GeminiService with the default configuration.
     */
    GeminiService() {
        this(new LlmConfig(Provider.GOOGLE_GEMINI, DEFAULT_BASE_URL, null));
    }

    /**
     * Constructs a GeminiService with the provided configuration.
     *
     * @param config the configuration settings for the service
     */
    GeminiService(LlmConfig config) {
        super(config, new GeminiInterceptor(config.getToken()));
        this.api = super.retrofit.create(GeminiApi.class);
    }

    /**
     * Parses a JSON chunk into a GenerateContentResponse object.
     *
     * @param json the JSON string to parse
     * @return the parsed GenerateContentResponse object
     * @throws StreamParsingException if an error occurs during parsing
     */
    private GenerateContentResponse parseChunk(String json) {
        ObjectMapper mapper = DefaultObjectMapper.get();
        try {
            return mapper.readValue(json, GenerateContentResponse.class);
        } catch (JsonProcessingException e) {
            throw new StreamParsingException("Error parsing JSON chunk", e);
        }
    }

    // ===============================
    // Models
    // ===============================

    public CompletableFuture<Model> getModel(String name) {
        return super.call(api.getModel(name));
    }

    public CompletableFuture<GeminiData<Model>> listModels() {
        return super.call(api.listModels());
    }

    public CompletableFuture<GeminiData<Model>> listModels(Integer pageSize, String pageToken) {
        return super.call(api.listModels(pageSize, pageToken));
    }

    // ===============================
    // Generating Content
    // ===============================

    public CompletableFuture<GenerateContentResponse> generateContent(String model, GenerateContentRequest request) {
        return super.call(api.generateContent(model, request));
    }

    public StreamingResponse<GenerateContentResponse> streamGenerateContent(String model, GenerateContentRequest request) {
        Call<ResponseBody> call = api.streamGenerateContent(model, request);
        return new StreamingResponse<>(super.stream(call, this::parseChunk));
    }

    // ===============================
    // Tokens
    // ===============================

    public CompletableFuture<CountTokensResponse> countTokens(String model, CountTokensRequest request) {
        return super.call(api.countTokens(model, request));
    }

    // ===============================
    // Files
    // ===============================

    @SneakyThrows
    public CompletableFuture<UploadMediaResponse> uploadMedia(UploadMediaRequest request, File file) {
        Long contentLength = file.length();
        String contentType = FileParser.getMimeType(file);

        return CompletableFuture.supplyAsync(() -> {
            try {
                Call<Void> call = api.startUpload(
                        "resumable",
                        "start",
                        contentLength,
                        contentType,
                        request
                );
                Response<Void> response = call.execute();
                if (!response.isSuccessful()) {
                    throw new UploadMediaException("Response isn't successful", new HttpException(response));
                }
                String uploadUrl = response.headers().get("X-Goog-Upload-URL");
                if (uploadUrl == null) {
                    throw new UploadMediaException("Upload url is null", new HttpException(response));
                }
                return uploadUrl;
            } catch (IOException e) {
                throw new UploadMediaException("Failed to start upload due to exception", e);
            }
        }).thenCompose(uploadUrl -> {
            RequestBody fileBody = FileParser.parseFile(file);
            return super.call(api.uploadMedia(uploadUrl, contentLength, 0L, "upload, finalize", fileBody));
        });
    }

    public CompletableFuture<UploadMediaResponse> uploadMediaMetadata(UploadMediaRequest request) {
        return super.call(api.uploadMediaMetadata(request));
    }

    public CompletableFuture<GeminiFile> getFile(String name) {
        return super.call(api.getFile(name));
    }

    public CompletableFuture<GeminiData<GeminiFile>> listFiles() {
        return super.call(api.listFiles());
    }

    public CompletableFuture<GeminiData<GeminiFile>> listFiles(Integer pageSize, String pageToken) {
        return super.call(api.listFiles(pageSize, pageToken));
    }

    public CompletableFuture<Void> deleteFile(String name) {
        return super.call(api.deleteFile(name));
    }

    // ===============================
    // Caching
    // ===============================

    public CompletableFuture<CachedContent> createCachedContent(CachedContent request) {
        return super.call(api.createCachedContents(request));
    }

    public CompletableFuture<GeminiData<CachedContent>> listCachedContent() {
        return super.call(api.listCachedContents());
    }

    public CompletableFuture<GeminiData<CachedContent>> listCachedContent(Integer pageSize, String pageToken) {
        return super.call(api.listCachedContents(pageSize, pageToken));
    }

    public CompletableFuture<CachedContent> getCachedContent(String name) {
        return super.call(api.getCachedContent(name));
    }

    public CompletableFuture<CachedContent> updateCachedContent(String name, CachedContent request) {
        return super.call(api.updateCachedContent(name, request));
    }

    public CompletableFuture<CachedContent> updateCachedContent(String name, CachedContent request, String updateMask) {
        return super.call(api.updateCachedContent(name, request, updateMask));
    }

    public CompletableFuture<Void> deleteCachedContent(String name) {
        return super.call(api.deleteCachedContent(name));
    }

    // ===============================
    // Embeddings
    // ===============================

    public CompletableFuture<EmbedContentResponse> embedContent(String model, EmbedContentRequest request) {
        return super.call(api.embedContent(model, request));
    }

    public CompletableFuture<BatchEmbedContentsResponse> batchEmbedContents(String model, BatchEmbedContentsRequest request) {
        return super.call(api.batchEmbedContents(model, request));
    }
}
