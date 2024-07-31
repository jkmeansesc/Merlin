package org.haifan.merlin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.haifan.merlin.internal.api.OpenAiApi;
import org.haifan.merlin.internal.constants.Fields;
import org.haifan.merlin.internal.constants.IanaMediaType;
import org.haifan.merlin.internal.constants.Provider;
import org.haifan.merlin.internal.interceptors.OpenAiInterceptor;
import org.haifan.merlin.internal.utils.DefaultObjectMapper;
import org.haifan.merlin.internal.utils.FileParser;
import org.haifan.merlin.model.StreamingResponse;
import org.haifan.merlin.model.openai.DeletionStatus;
import org.haifan.merlin.model.openai.OpenAiData;
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
import org.haifan.merlin.model.openai.endpoints.chat.ChatCompletionChunk;
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
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Service class for interacting with the OpenAI API.
 * This class extends {@link LlmService} and provides methods to call endpoints in {@link OpenAiApi}.
 */
public class OpenAiService extends LlmService {

    private final OpenAiApi api;
    public static final String DEFAULT_BASE_URL = "https://api.openai.com";

    /**
     * Constructs an {@code OpenAiService} instance with the default configuration.
     * Uses the default base URL and retrieves the token from the environment or configuration.
     */
    OpenAiService() {
        this(new LlmConfig(Provider.OPENAI, DEFAULT_BASE_URL, null));
    }

    /**
     * Constructs an {@code OpenAiService} instance with the specified configuration.
     *
     * @param config the configuration for the OpenAI service.
     */
    OpenAiService(LlmConfig config) {
        super(config, new OpenAiInterceptor(config.getToken()));
        this.api = super.retrofit.create(OpenAiApi.class);
    }

    private ChatCompletionChunk parseChunk(String json) {
        ObjectMapper mapper = DefaultObjectMapper.get();
        try {
            return mapper.readValue(json, ChatCompletionChunk.class);
        } catch (JsonProcessingException e) {
            throw new StreamParsingException("Error parsing JSON chunk", e);
        }
    }

    private Run parseRun(String json) {
        ObjectMapper mapper = DefaultObjectMapper.get();
        try {
            return mapper.readValue(json, Run.class);
        } catch (JsonProcessingException e) {
            throw new StreamParsingException("Error parsing JSON chunk", e);
        }
    }

    private void addIfNotNull(MultipartBody.Builder builder, String name, String value) {
        if (value != null) {
            builder.addFormDataPart(name, value);
        }
    }

    // ===============================
    // ENDPOINTS - Audio
    // ===============================

    public CompletableFuture<ResponseBody> createSpeech(SpeechRequest request) {
        return super.call(api.createSpeech(request));
    }

    public CompletableFuture<Transcription> createTranscription(TranscriptionRequest request, String audioPath) {
        java.io.File file = new java.io.File(audioPath);
        return createTranscription(request, file);
    }

    public CompletableFuture<Transcription> createTranscription(TranscriptionRequest request, File audio) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(Fields.FILE, audio.getName(), FileParser.parseFile(audio))
                .addFormDataPart(Fields.MODEL, request.getModel());
        addIfNotNull(builder, Fields.PROMPT, request.getPrompt());
        addIfNotNull(builder, Fields.LANGUAGE, request.getLanguage());
        addIfNotNull(builder, Fields.RESPONSE_FORMAT, request.getResponseFormat());
        addIfNotNull(builder, Fields.TEMPERATURE, request.getTemperature() != null ? request.getTemperature().toString() : null);
        addIfNotNull(builder, Fields.TIMESTAMP_GRANULARITIES, request.getTimestampGranularities() != null ? Arrays.toString(request.getTimestampGranularities()) : null);

        return super.call(api.createTranscription(builder.build()));
    }

    public CompletableFuture<Translation> createTranslation(TranslationRequest request, String audioPath) {
        java.io.File file = new java.io.File(audioPath);
        return createTranslation(request, file);
    }

    public CompletableFuture<Translation> createTranslation(TranslationRequest request, File audio) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(Fields.FILE, audio.getName(), FileParser.parseFile(audio))
                .addFormDataPart(Fields.MODEL, request.getModel());
        addIfNotNull(builder, Fields.PROMPT, request.getPrompt());
        addIfNotNull(builder, Fields.TEMPERATURE, request.getTemperature() != null ? request.getTemperature().toString() : null);
        addIfNotNull(builder, Fields.RESPONSE_FORMAT, request.getResponseFormat());
        return super.call(api.createTranslation(builder.build()));
    }

    // ===============================
    // ENDPOINTS - Chat
    // ===============================

    public CompletableFuture<ChatCompletion> createChatCompletion(ChatCompletionRequest request) {
        return super.call(api.createChatCompletion(request));
    }

    public StreamingResponse<ChatCompletionChunk> streamChatCompletion(ChatCompletionRequest request) {
        request.setStream(true);
        Call<ResponseBody> call = api.streamChatCompletion(request);
        return new StreamingResponse<>(super.stream(call, this::parseChunk));
    }

    // ===============================
    // ENDPOINTS - Embeddings
    // ===============================

    public CompletableFuture<OpenAiData<Embedding>> createEmbeddings(EmbeddingRequest request) {
        return super.call(api.createEmbeddings(request));
    }

    // ===============================
    // ENDPOINTS - Fine-tuning
    // ===============================

    public CompletableFuture<FineTuningJob> createFineTuningJob(FineTuningJobRequest request) {
        return super.call(api.createFineTuningJob(request));
    }

    public CompletableFuture<OpenAiData<FineTuningJob>> listFineTuningJobs() {
        return super.call(api.listFineTuningJobs());
    }

    public CompletableFuture<OpenAiData<FineTuningJob>> listFineTuningJobs(String after, Integer limit) {
        return super.call(api.listFineTuningJobs(after, limit));
    }

    public CompletableFuture<OpenAiData<FineTuningEvent>> listFineTuningEvents(String fineTuningJobId) {
        return super.call(api.listFineTuningEvents(fineTuningJobId));
    }

    public CompletableFuture<OpenAiData<FineTuningEvent>> listFineTuningEvents(String fineTuningJobId, String after, Integer limit) {
        return super.call(api.listFineTuningEvents(fineTuningJobId, after, limit));
    }

    public CompletableFuture<OpenAiData<FineTuningCheckpoint>> listFineTuningCheckpoints(String fineTuningJobId) {
        return super.call(api.listFineTuningCheckpoints(fineTuningJobId));
    }

    public CompletableFuture<OpenAiData<FineTuningCheckpoint>> listFineTuningCheckpoints(String fineTuningJobId, String after, Integer limit) {
        return super.call(api.listFineTuningCheckpoints(fineTuningJobId, after, limit));
    }

    public CompletableFuture<FineTuningJob> retrieveFineTuningJob(String fineTuningJobId) {
        return super.call(api.retrieveFineTuningJob(fineTuningJobId));
    }

    public CompletableFuture<FineTuningJob> cancelFineTuningJob(String fineTuningJobId) {
        return super.call(api.cancelFineTuningJob(fineTuningJobId));
    }

    // ===============================
    // ENDPOINTS - Batch
    // ===============================

    public CompletableFuture<Batch> createBatch(BatchRequest request) {
        return super.call(api.createBatch(request));
    }

    public CompletableFuture<Batch> retrieveBatch(String batchId) {
        return super.call(api.retrieveBatch(batchId));
    }

    public CompletableFuture<Batch> cancelBatch(String batchId) {
        return super.call(api.cancelBatch(batchId));
    }

    public CompletableFuture<OpenAiData<Batch>> listBatches() {
        return super.call(api.listBatches());
    }

    public CompletableFuture<OpenAiData<Batch>> listBatches(String after, Integer limit) {
        return super.call(api.listBatches(after, limit));
    }

    // ===============================
    // ENDPOINTS - Files
    // ===============================

    public CompletableFuture<OpenAiFile> uploadFile(String purpose, String filePath) {
        java.io.File file = new java.io.File(filePath);
        return uploadFile(purpose, file);
    }

    public CompletableFuture<OpenAiFile> uploadFile(String purpose, File file) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart(Fields.PURPOSE, purpose).addFormDataPart(Fields.FILE, file.getName(), FileParser.parseFile(file));

        return super.call(api.uploadFile(builder.build()));
    }

    public CompletableFuture<OpenAiData<OpenAiFile>> listFiles() {
        return super.call(api.listFiles());
    }

    public CompletableFuture<OpenAiFile> retrieveFile(String fileId) {
        return super.call(api.retrieveFile(fileId));
    }

    public CompletableFuture<DeletionStatus> deleteFile(String fileId) {
        return super.call(api.deleteFile(fileId));
    }

    public CompletableFuture<ResponseBody> retrieveFileContent(String fileId) {
        return super.call(api.retrieveFileContent(fileId));
    }

    // ===============================
    // ENDPOINTS - Images
    // ===============================

    public CompletableFuture<OpenAiData<Image>> createImage(ImageRequest imageRequest) {
        return super.call(api.createImage(imageRequest));
    }

    public CompletableFuture<OpenAiData<Image>> createImageEdit(ImageEditRequest request, String imagePath, String maskPath) {
        File image = new File(imagePath);
        File mask = maskPath != null ? new File(maskPath) : null;
        return createImageEdit(request, image, mask);
    }

    public CompletableFuture<OpenAiData<Image>> createImageEdit(ImageEditRequest request, File image, File mask) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(Fields.PROMPT, request.getPrompt())
                .addFormDataPart(Fields.IMAGE, image.getName(), RequestBody.create(image, MediaType.parse(IanaMediaType.IMAGE_ALL)));

        if (mask != null) {
            builder.addFormDataPart(Fields.MASK, mask.getName(), RequestBody.create(mask, MediaType.parse(IanaMediaType.IMAGE_ALL)));
        }

        addIfNotNull(builder, Fields.N, request.getN() != null ? request.getN().toString() : null);
        addIfNotNull(builder, Fields.SIZE, request.getSize());
        addIfNotNull(builder, Fields.RESPONSE_FORMAT, request.getResponseFormat());
        addIfNotNull(builder, Fields.MODEL, request.getModel());
        addIfNotNull(builder, Fields.USER, request.getUser());

        return super.call(api.createImageEdit(builder.build()));
    }

    public CompletableFuture<OpenAiData<Image>> createImageVariation(ImageVariationRequest request, String imagePath) {
        java.io.File image = new java.io.File(imagePath);
        return createImageVariation(request, image);
    }

    public CompletableFuture<OpenAiData<Image>> createImageVariation(ImageVariationRequest request, File image) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart(Fields.N, request.getN().toString()) // integer or null
                .addFormDataPart(Fields.IMAGE, image.getName(), RequestBody.create(image, MediaType.parse(IanaMediaType.IMAGE_ALL)));

        addIfNotNull(builder, Fields.N, request.getN() != null ? request.getN().toString() : null);
        addIfNotNull(builder, Fields.SIZE, request.getSize());
        addIfNotNull(builder, Fields.RESPONSE_FORMAT, request.getResponseFormat());
        addIfNotNull(builder, Fields.MODEL, request.getModel());
        addIfNotNull(builder, Fields.USER, request.getUser());

        return super.call(api.createImageVariation(builder.build()));
    }

    // ===============================
    // ENDPOINTS - Models
    // ===============================

    /**
     * Lists the currently available models, and provides basic information about each one such as the owner and availability.
     *
     * @return a list of model object.
     * @see Model
     */
    public CompletableFuture<OpenAiData<Model>> listModels() {
        return super.call(api.listModels());
    }

    /**
     * Retrieves a model instance, providing basic information about the model such as the owner and permission.
     *
     * @param model The ID of the model to use for this request
     * @return The model object matching the specified ID.
     * @see Model
     */
    public CompletableFuture<Model> retrieveModel(String model) {
        return super.call(api.retrieveModel(model));
    }

    /**
     * Delete a fine-tuned model. You must have the Owner role in your organization to delete a model.
     *
     * @param model the model to delete
     * @return deletion status
     * @see DeletionStatus
     */
    public CompletableFuture<DeletionStatus> deleteAFineTunedModel(String model) {
        return super.call(api.deleteAFineTunedModel(model));
    }

    // ===============================
    // ENDPOINTS - Moderation
    // ===============================

    public CompletableFuture<ModerationList> createModeration(ModerationRequest moderationRequest) {
        return super.call(api.createModeration(moderationRequest));
    }

    // ===============================
    // ASSISTANTS - Assistants
    // ===============================

    public CompletableFuture<Assistant> createAssistant(AssistantRequest request) {
        return super.call(api.createAssistant(request));
    }

    public CompletableFuture<OpenAiData<Assistant>> listAssistants() {
        return super.call(api.listAssistants());
    }

    public CompletableFuture<OpenAiData<Assistant>> listAssistants(Map<String, String> queryMap) {
        return super.call(api.listAssistants(queryMap));
    }

    public CompletableFuture<Assistant> retrieveAssistant(String assistantId) {
        return super.call(api.retrieveAssistant(assistantId));
    }

    public CompletableFuture<Assistant> modifyAssistant(String assistantId, AssistantRequest request) {
        return super.call(api.modifyAssistant(assistantId, request));
    }

    public CompletableFuture<DeletionStatus> deleteAssistant(String assistantId) {
        return super.call(api.deleteAssistant(assistantId));
    }

    // ===============================
    // ASSISTANTS - Threads
    // ===============================

    public CompletableFuture<OpenAiThread> createThread(@Body ThreadRequest request) {
        return super.call(api.createThread(request));
    }

    public CompletableFuture<OpenAiThread> retrieveThread(String threadId) {
        return super.call(api.retrieveThread(threadId));
    }

    public CompletableFuture<OpenAiThread> modifyThread(String threadId, ThreadRequest request) {
        return super.call(api.modifyThread(threadId, request));
    }

    public CompletableFuture<DeletionStatus> deleteThread(String threadId) {
        return super.call(api.deleteThread(threadId));
    }

    // ===============================
    // ASSISTANTS - Messages
    // ===============================

    public CompletableFuture<MessageObject> createMessage(String threadId, MessageRequest request) {
        return super.call(api.createMessage(threadId, request));
    }

    public CompletableFuture<OpenAiData<MessageObject>> listMessages(String threadId) {
        return super.call(api.listMessages(threadId));
    }

    public CompletableFuture<OpenAiData<MessageObject>> listMessages(String threadId, Map<String, String> queryMap) {
        return super.call(api.listMessages(threadId, queryMap));
    }

    public CompletableFuture<MessageObject> retrieveMessage(String threadId, String messageId) {
        return super.call(api.retrieveMessage(threadId, messageId));
    }

    public CompletableFuture<MessageObject> modifyMessage(String threadId, String messageId, MessageRequest request) {
        return super.call(api.modifyMessage(threadId, messageId, request));
    }

    public CompletableFuture<DeletionStatus> deleteMessage(String threadId, String messageId) {
        return super.call(api.deleteMessage(threadId, messageId));
    }

    // ===============================
    // ASSISTANTS - Runs
    // ===============================

    public CompletableFuture<Run> createRun(String threadId, RunRequest request) {
        return super.call(api.createRun(threadId, request));
    }

    public CompletableFuture<Run> createThreadAndRun(RunRequest request) {
        return super.call(api.createThreadAndRun(request));
    }

    public CompletableFuture<OpenAiData<Run>> listRuns(String threadId) {
        return super.call(api.listRuns(threadId));
    }

    public CompletableFuture<OpenAiData<Run>> listRuns(String threadId, Map<String, String> queryMap) {
        return super.call(api.listRuns(threadId, queryMap));
    }

    public CompletableFuture<Run> retrieveRun(String threadId, String runId) {
        return super.call(api.retrieveRun(threadId, runId));
    }

    public CompletableFuture<Run> modifyRun(String threadId, String runId, RunRequest request) {
        return super.call(api.modifyRun(threadId, runId, request));
    }

    public CompletableFuture<Run> submitToolOutputsToRun(String threadId, String runId, ToolOutputRequest request) {
        request.setStream(false);
        return super.call(api.submitToolOutputsToRun(threadId, runId, request));
    }

    public StreamingResponse<Run> streamToolOutputsToRun(String threadId, String runId, ToolOutputRequest request) {
        request.setStream(true);
        Call<ResponseBody> call = api.streamToolOutputsToRun(threadId, runId, request);
        return new StreamingResponse<>(super.stream(call, this::parseRun));
    }

    public CompletableFuture<Run> cancelRun(String threadId, String runId) {
        return super.call(api.cancelRun(threadId, runId));
    }

    // ===============================
    // ASSISTANTS - Run Steps
    // ===============================

    public CompletableFuture<OpenAiData<RunStep>> listRunSteps(@Path("thread_id") String threadId, @Path("run_id") String runId) {
        return super.call(api.listRunSteps(threadId, runId));
    }

    public CompletableFuture<OpenAiData<RunStep>> listRunSteps(@Path("thread_id") String threadId, @Path("run_id") String runId, @QueryMap Map<String, String> queryMap) {
        return super.call(api.listRunSteps(threadId, runId, queryMap));
    }

    public CompletableFuture<RunStep> retrieveRunStep(@Path("thread_id") String threadId, @Path("run_id") String runId, @Path("step_id") String stepId) {
        return super.call(api.retrieveRunStep(threadId, runId, stepId));
    }

    // ===============================
    // ASSISTANTS - Vector Stores
    // ===============================

    public CompletableFuture<VectorStore> createVectorStore(VectorStoreRequest vectorStoreRequest) {
        return super.call(api.createVectorStore(vectorStoreRequest));
    }

    public CompletableFuture<OpenAiData<VectorStore>> listVectorStores() {
        return super.call(api.listVectorStores());
    }

    public CompletableFuture<OpenAiData<VectorStore>> listVectorStores(Map<String, String> queryMap) {
        return super.call(api.listVectorStores(queryMap));
    }

    public CompletableFuture<VectorStore> retrieveVectorStore(String vectorStoreId) {
        return super.call(api.retrieveVectorStore(vectorStoreId));
    }

    /**
     * only these fields are supported for this particular endpoint: name, expires_after, metadata
     * <a href="https://platform.openai.com/docs/api-reference/vector-stores/modify">...</a>
     */
    public CompletableFuture<VectorStore> modifyVectorStore(String vectorStoreId, VectorStoreRequest vectorStoreRequest) {
        return super.call(api.modifyVectorStore(vectorStoreId, vectorStoreRequest));
    }

    public CompletableFuture<DeletionStatus> deleteVectorStore(String vectorStoreId) {
        return super.call(api.deleteVectorStore(vectorStoreId));
    }

    // ===============================
    // ASSISTANTS - Vector Stores Files
    // ===============================

    public CompletableFuture<VectorStoreFile> createVectorStoreFile(String vectorStoreId, VectorStoreFileRequest request) {
        return super.call(api.createVectorStoreFile(vectorStoreId, request));
    }

    public CompletableFuture<OpenAiData<VectorStoreFile>> listVectorStoreFiles(String vectorStoreId) {
        return super.call(api.listVectorStoreFiles(vectorStoreId));
    }

    public CompletableFuture<OpenAiData<VectorStoreFile>> listVectorStoreFiles(String vectorStoreId, Map<String, String> queryMap) {
        return super.call(api.listVectorStoreFiles(vectorStoreId, queryMap));
    }

    public CompletableFuture<VectorStoreFile> retrieveVectorStoreFile(String vectorStoreId, String fileId) {
        return super.call(api.retrieveVectorStoreFile(vectorStoreId, fileId));
    }

    public CompletableFuture<DeletionStatus> deleteVectorStoreFile(String vectorStoreId, String fileId) {
        return super.call(api.deleteVectorStoreFile(vectorStoreId, fileId));
    }

    // ===============================
    // ASSISTANTS - Vector Stores File Batches
    // ===============================

    public CompletableFuture<VectorStoreFile> createVectorStoreFileBatch(String vectorStoreId, VectorStoreRequest request) {
        return super.call(api.createVectorStoreFileBatch(vectorStoreId, request));
    }

    public CompletableFuture<VectorStoreFileBatch> retrieveVectorStoreFileBatch(String vectorStoreId, String batchId) {
        return super.call(api.retrieveVectorStoreFileBatch(vectorStoreId, batchId));
    }

    public CompletableFuture<VectorStoreFileBatch> cancelVectorStoreFileBatch(String vectorStoreId, String batchId) {
        return super.call(api.cancelVectorStoreFileBatch(vectorStoreId, batchId));
    }

    public CompletableFuture<OpenAiData<VectorStoreFileBatch>> listVectorStoreFileBatch(String vectorStoreId, String batchId) {
        return super.call(api.listVectorStoreFileBatch(vectorStoreId, batchId));
    }

    public CompletableFuture<OpenAiData<VectorStoreFileBatch>> listVectorStoreFileBatch(String vectorStoreId, String batchId, Map<String, String> queryMap) {
        return super.call(api.listVectorStoreFileBatch(vectorStoreId, batchId, queryMap));
    }
}
