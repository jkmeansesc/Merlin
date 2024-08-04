package org.merlin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.merlin.internal.api.OllamaApi;
import org.merlin.internal.constants.IanaMediaType;
import org.merlin.internal.constants.Provider;
import org.merlin.internal.interceptors.OllamaInterceptor;
import org.merlin.internal.utils.DefaultObjectMapper;
import org.merlin.model.StreamingResponse;
import org.merlin.model.ollama.*;
import retrofit2.Call;
import retrofit2.http.Body;

import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * Service implementation for interacting with the Ollama Language Model API.
 * Extends the base LlmService to provide methods for model management, content generation, and streaming responses.
 */
public class OllamaService extends LlmService {

    private final OllamaApi api;
    public static final String DEFAULT_BASE_URL = "http://localhost:11434/";

    /**
     * Constructs an OllamaService with the default configuration.
     */
    OllamaService() {
        this(new LlmConfig(Provider.OLLAMA, DEFAULT_BASE_URL, null));
    }

    /**
     * Constructs an OllamaService with the provided configuration.
     *
     * @param config the configuration settings for the service
     */
    OllamaService(LlmConfig config) {
        super(config, new OllamaInterceptor());
        this.api = super.retrofit.create(OllamaApi.class);
    }

    /**
     * Parses a JSON chunk into an OllamaCompletion object.
     *
     * @param json the JSON string to parse
     * @return the parsed OllamaCompletion object
     * @throws StreamParsingException if an error occurs during parsing
     */
    private OllamaCompletion parseChunk(String json) {
        ObjectMapper mapper = DefaultObjectMapper.get();
        try {
            return mapper.readValue(json, OllamaCompletion.class);
        } catch (JsonProcessingException e) {
            throw new StreamParsingException("Error parsing JSON chunk", e);
        }
    }

    /**
     * Parses a JSON chunk into an OllamaStatus object.
     *
     * @param json the JSON string to parse
     * @return the parsed OllamaStatus object
     * @throws StreamParsingException if an error occurs during parsing
     */
    private OllamaStatus parseStatus(String json) {
        ObjectMapper mapper = DefaultObjectMapper.get();
        try {
            return mapper.readValue(json, OllamaStatus.class);
        } catch (JsonProcessingException e) {
            throw new StreamParsingException("Error parsing JSON chunk", e);
        }
    }

    /**
     * Streams content generation using the specified request.
     *
     * @param request the request containing the parameters for content generation
     * @return a StreamingResponse emitting OllamaCompletion chunks
     */
    public StreamingResponse<OllamaCompletion> streamCompletion(OllamaCompletionRequest request) {
        Call<ResponseBody> call = api.streamCompletion(request);
        return new StreamingResponse<>(super.stream(call, this::parseChunk));
    }

    /**
     * Creates a content completion using the specified request.
     *
     * @param request the request containing the parameters for content completion
     * @return a CompletableFuture containing the OllamaCompletion
     */
    public CompletableFuture<OllamaCompletion> createCompletion(OllamaCompletionRequest request) {
        request.setStream(false);
        return super.call(api.createCompletion(request));
    }

    /**
     * Creates a chat completion using the specified request.
     *
     * @param request the request containing the parameters for chat completion
     * @return a CompletableFuture containing the OllamaCompletion
     */
    public CompletableFuture<OllamaCompletion> createChatCompletion(OllamaCompletionRequest request) {
        request.setStream(false);
        return super.call(api.createChatCompletion(request));
    }

    /**
     * Streams chat completion using the specified request.
     *
     * @param request the request containing the parameters for chat completion
     * @return a StreamingResponse emitting OllamaCompletion chunks
     */
    public StreamingResponse<OllamaCompletion> streamChatCompletion(@Body OllamaCompletionRequest request) {
        Call<ResponseBody> call = api.streamChatCompletion(request);
        return new StreamingResponse<>(super.stream(call, this::parseChunk));
    }

    /**
     * Creates a model stream using the specified request.
     *
     * @param request the request containing the parameters for model creation
     * @return a StreamingResponse emitting OllamaStatus chunks
     */
    public StreamingResponse<OllamaStatus> createModelStream(OllamaCompletionRequest request) {
        Call<ResponseBody> call = api.createModelStream(request);
        return new StreamingResponse<>(super.stream(call, this::parseStatus));
    }

    /**
     * Creates a model using the specified request.
     *
     * @param request the request containing the parameters for model creation
     * @return a CompletableFuture containing the OllamaStatus
     */
    public CompletableFuture<OllamaStatus> createModel(OllamaCompletionRequest request) {
        request.setStream(false);
        return super.call(api.createModel(request));
    }

    /**
     * Checks a blob by its digest.
     *
     * @param digest the digest of the blob to check
     * @return a CompletableFuture representing the completion of the check
     */
    public CompletableFuture<Void> checkBlob(String digest) {
        return super.call(api.checkBlob(digest));
    }

    /**
     * Creates a blob with the specified digest and file.
     *
     * @param digest the digest of the blob to create
     * @param file   the file to upload as the blob
     * @return a CompletableFuture representing the completion of the blob creation
     */
    public CompletableFuture<Void> createBlob(String digest, File file) {
        RequestBody requestBody = RequestBody.create(file, MediaType.parse(IanaMediaType.OCTET_STREAM));
        return super.call(api.createBlob(digest, requestBody));
    }

    /**
     * Lists all models.
     *
     * @return a CompletableFuture containing an OllamaModelList
     */
    public CompletableFuture<OllamaModelList> listModels() {
        return super.call(api.listModels());
    }

    /**
     * Shows information for a specific model.
     *
     * @param request the request containing the parameters for showing model information
     * @return a CompletableFuture containing the OllamaModel
     */
    public CompletableFuture<OllamaModel> showModelInfo(OllamaCompletionRequest request) {
        return super.call(api.showModelInfo(request));
    }

    /**
     * Copies a model using the specified request.
     *
     * @param request the request containing the parameters for copying the model
     * @return a CompletableFuture representing the completion of the copy operation
     */
    public CompletableFuture<Void> copyModel(OllamaCompletionRequest request) {
        return super.call(api.copyModel(request));
    }

    /**
     * Deletes a model using the specified request.
     *
     * @param request the request containing the parameters for deleting the model
     * @return a CompletableFuture representing the completion of the delete operation
     */
    public CompletableFuture<Void> deleteModel(OllamaCompletionRequest request) {
        return super.call(api.deleteModel(request));
    }

    /**
     * Pulls a model stream using the specified request.
     *
     * @param request the request containing the parameters for pulling the model stream
     * @return a StreamingResponse emitting OllamaStatus chunks
     */
    public StreamingResponse<OllamaStatus> pullModelStream(OllamaCompletionRequest request) {
        Call<ResponseBody> call = api.pullModelStream(request);
        return new StreamingResponse<>(super.stream(call, this::parseStatus));
    }

    /**
     * Pulls a model using the specified request.
     *
     * @param request the request containing the parameters for pulling the model
     * @return a CompletableFuture containing the OllamaStatus
     */
    public CompletableFuture<OllamaStatus> pullModel(OllamaCompletionRequest request) {
        request.setStream(false);
        return super.call(api.pullModel(request));
    }

    /**
     * Pushes a model stream using the specified request.
     *
     * @param request the request containing the parameters for pushing the model stream
     * @return a StreamingResponse emitting OllamaStatus chunks
     */
    public StreamingResponse<OllamaStatus> pushModelStream(OllamaCompletionRequest request) {
        Call<ResponseBody> call = api.pushModelStream(request);
        return new StreamingResponse<>(super.stream(call, this::parseStatus));
    }

    /**
     * Pushes a model using the specified request.
     *
     * @param request the request containing the parameters for pushing the model
     * @return a CompletableFuture containing the OllamaStatus
     */
    public CompletableFuture<OllamaStatus> pushModel(OllamaCompletionRequest request) {
        request.setStream(false);
        return super.call(api.pushModel(request));
    }

    /**
     * Creates an embedding using the specified request.
     *
     * @param request the request containing the parameters for creating the embedding
     * @return a CompletableFuture containing the OllamaEmbedding
     */
    public CompletableFuture<OllamaEmbedding> createEmbedding(OllamaCompletionRequest request) {
        return super.call(api.createEmbedding(request));
    }

    /**
     * Lists all running models.
     *
     * @return a CompletableFuture containing an OllamaModelList of running models
     */
    public CompletableFuture<OllamaModelList> listRunning() {
        return super.call(api.listRunning());
    }
}
