package org.haifan.merlin.service;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import org.haifan.merlin.api.OpenAiApi;
import org.haifan.merlin.config.LlmConfig;
import org.haifan.merlin.config.OpenAiConfig;
import org.haifan.merlin.constants.Fields;
import org.haifan.merlin.model.openai.assistants.vectorstores.VectorStore;
import org.haifan.merlin.model.openai.assistants.vectorstores.VectorStoreFile;
import org.haifan.merlin.model.openai.assistants.vectorstores.VectorStoreFileBatch;
import org.haifan.merlin.model.openai.assistants.vectorstores.VectorStoreRequest;
import org.haifan.merlin.model.openai.DeletionStatus;
import org.haifan.merlin.model.openai.OpenAiList;
import org.haifan.merlin.model.openai.StreamingResponse;
import org.haifan.merlin.model.openai.endpoints.audio.*;
import org.haifan.merlin.model.openai.endpoints.batch.Batch;
import org.haifan.merlin.model.openai.endpoints.batch.BatchRequest;
import org.haifan.merlin.model.openai.endpoints.chat.ChatCompletion;
import org.haifan.merlin.model.openai.endpoints.chat.ChatCompletionChunk;
import org.haifan.merlin.model.openai.endpoints.chat.ChatCompletionRequest;
import org.haifan.merlin.model.openai.endpoints.embeddings.Embedding;
import org.haifan.merlin.model.openai.endpoints.embeddings.EmbeddingRequest;
import org.haifan.merlin.model.openai.endpoints.finetune.FineTuningCheckpoint;
import org.haifan.merlin.model.openai.endpoints.finetune.FineTuningEvent;
import org.haifan.merlin.model.openai.endpoints.finetune.FineTuningJob;
import org.haifan.merlin.model.openai.endpoints.finetune.FineTuningJobRequest;
import org.haifan.merlin.model.openai.endpoints.images.ImageEditRequest;
import org.haifan.merlin.model.openai.endpoints.images.ImageRequest;
import org.haifan.merlin.model.openai.endpoints.images.ImageVariationRequest;
import org.haifan.merlin.model.openai.endpoints.images.ImageList;
import org.haifan.merlin.model.openai.endpoints.models.Model;
import org.haifan.merlin.model.openai.endpoints.moderations.ModerationList;
import org.haifan.merlin.model.openai.endpoints.moderations.ModerationRequest;
import org.haifan.merlin.constants.IanaMediaType;
import org.haifan.merlin.interceptors.OpenAiInterceptor;
import org.haifan.merlin.utils.FileParser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * TODO: add javadoc
 */
public class OpenAiService extends LlmService {

    private final OpenAiApi api;

    public OpenAiService() {
        this(new OpenAiConfig());
    }

    public OpenAiService(String apiKey) {
        this(new OpenAiConfig(apiKey));
    }

    private OpenAiService(OpenAiConfig config) {
        super(config, new OpenAiInterceptor(config.getApiKey()));
        this.api = super.retrofit.create(OpenAiApi.class);
    }

    @Override
    public LlmConfig getConfig() {
        return super.llmConfig;
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
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(Fields.FILE, audio.getName(), FileParser.parseFile(audio))
                .addFormDataPart(Fields.MODEL, request.getModel());

        if (request.getPrompt() != null) {
            builder.addFormDataPart(Fields.PROMPT, request.getPrompt());
        }
        if (request.getLanguage() != null) {
            builder.addFormDataPart(Fields.LANGUAGE, request.getLanguage());
        }
        if (request.getResponseFormat() != null) {
            builder.addFormDataPart(Fields.RESPONSE_FORMAT, request.getResponseFormat());
        }
        if (request.getTemperature() != null) {
            builder.addFormDataPart(Fields.TEMPERATURE, request.getTemperature().toString());
        }
        if (request.getTimestampGranularities() != null) {
            builder.addFormDataPart(Fields.TIMESTAMP_GRANULARITIES, Arrays.toString(request.getTimestampGranularities()));
        }

        return super.call(api.createTranscription(builder.build()));
    }

    public CompletableFuture<Translation> createTranslation(TranslationRequest request, String audioPath) {
        java.io.File file = new java.io.File(audioPath);
        return createTranslation(request, file);
    }

    public CompletableFuture<Translation> createTranslation(TranslationRequest request, File audio) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(Fields.FILE, audio.getName(), FileParser.parseFile(audio))
                .addFormDataPart(Fields.MODEL, request.getModel());

        if (request.getPrompt() != null) {
            builder.addFormDataPart(Fields.PROMPT, request.getPrompt());
        }
        if (request.getResponseFormat() != null) {
            builder.addFormDataPart(Fields.RESPONSE_FORMAT, request.getResponseFormat());
        }
        if (request.getTemperature() != null) {
            builder.addFormDataPart(Fields.TEMPERATURE, request.getTemperature().toString());
        }

        return super.call(api.createTranslation(builder.build()));
    }

    // ===============================
    // ENDPOINTS - Chat
    // ===============================

    public CompletableFuture<ChatCompletion> createChatCompletion(ChatCompletionRequest request) {
        return super.call(api.createChatCompletion(request));
    }

    public StreamingResponse<ChatCompletionChunk> streamChatCompletion(ChatCompletionRequest request) {
        Call<ResponseBody> call = api.streamChatCompletion(request);
        return new StreamingResponse<>(call, this::parseChunk);
    }

    // TODO: implement this
    private ChatCompletionChunk parseChunk(String json) {
        // Use Jackson or Gson to parse JSON into ChatCompletionChunk
        return null;
    }

    // ===============================
    // ENDPOINTS - Embeddings
    // ===============================

    public CompletableFuture<Embedding> createEmbeddings(EmbeddingRequest request) {
        return super.call(api.createEmbeddings(request));
    }

    // ===============================
    // ENDPOINTS - Fine-tuning
    // ===============================

    public CompletableFuture<FineTuningJob> createFineTuningJob(FineTuningJobRequest request) {
        return super.call(api.createFineTuningJob(request));
    }

    public CompletableFuture<OpenAiList<FineTuningJob>> listFineTuningJobs() {
        return super.call(api.listFineTuningJobs());
    }

    public CompletableFuture<OpenAiList<FineTuningJob>> listFineTuningJobs(String after, Integer limit) {
        return super.call(api.listFineTuningJobs(after, limit));
    }

    public CompletableFuture<OpenAiList<FineTuningEvent>> listFineTuningEvents(String fineTuningJobId) {
        return super.call(api.listFineTuningEvents(fineTuningJobId));
    }

    public CompletableFuture<OpenAiList<FineTuningEvent>> listFineTuningEvents(String fineTuningJobId, String after, Integer limit) {
        return super.call(api.listFineTuningEvents(fineTuningJobId, after, limit));
    }

    public CompletableFuture<OpenAiList<FineTuningCheckpoint>> listFineTuningCheckpoints(String fineTuningJobId) {
        return super.call(api.listFineTuningCheckpoints(fineTuningJobId));
    }

    public CompletableFuture<OpenAiList<FineTuningCheckpoint>> listFineTuningCheckpoints(String fineTuningJobId, String after, Integer limit) {
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

    public CompletableFuture<OpenAiList<Batch>> listBatches() {
        return super.call(api.listBatches());
    }

    public CompletableFuture<OpenAiList<Batch>> listBatches(String after, Integer limit) {
        return super.call(api.listBatches(after, limit));
    }

    // ===============================
    // ENDPOINTS - Files
    // ===============================

    public CompletableFuture<org.haifan.merlin.model.openai.endpoints.files.File> uploadFile(String purpose, String filePath) {
        java.io.File file = new java.io.File(filePath);
        return uploadFile(purpose, file);
    }

    public CompletableFuture<org.haifan.merlin.model.openai.endpoints.files.File> uploadFile(String purpose, File file) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(Fields.PURPOSE, purpose)
                .addFormDataPart(Fields.FILE, file.getName(), FileParser.parseFile(file));

        return super.call(api.uploadFile(builder.build()));
    }

    public CompletableFuture<OpenAiList<org.haifan.merlin.model.openai.endpoints.files.File>> listFiles() {
        return super.call(api.listFiles());
    }

    public CompletableFuture<org.haifan.merlin.model.openai.endpoints.files.File> retrieveFile(String fileId) {
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

    public CompletableFuture<ImageList> createImage(ImageRequest imageRequest) {
        return super.call(api.createImage(imageRequest));
    }


    public CompletableFuture<ImageList> createImageEdit(ImageEditRequest request, String imagePath, String maskPath) {
        File image = new File(imagePath);
        File mask = maskPath != null ? new File(maskPath) : null;
        return createImageEdit(request, image, mask);
    }

    private CompletableFuture<ImageList> createImageEdit(ImageEditRequest request, File image, File mask) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(Fields.N, request.getN().toString()) // integer or null
                .addFormDataPart(Fields.SIZE, request.getSize()) // string or null
                .addFormDataPart(Fields.RESPONSE_FORMAT, request.getResponseFormat()) //string or null
                .addFormDataPart(Fields.PROMPT, request.getPrompt())
                .addFormDataPart(Fields.IMAGE, image.getName(), RequestBody.create(image, MediaType.parse(IanaMediaType.IMAGE_ALL)));

        if (mask != null) {
            builder.addFormDataPart(Fields.MASK, mask.getName(), RequestBody.create(mask, MediaType.parse(IanaMediaType.IMAGE_ALL)));
        }
        if (request.getModel() != null) {
            builder.addFormDataPart(Fields.MODEL, request.getModel());
        }
        if (request.getUser() != null) {
            builder.addFormDataPart(Fields.USER, request.getUser());
        }

        return super.call(api.createImageEdit(builder.build()));
    }

    public CompletableFuture<ImageList> createImageVariation(ImageVariationRequest request, String imagePath) {
        java.io.File image = new java.io.File(imagePath);
        return createImageVariation(request, image);
    }

    public CompletableFuture<ImageList> createImageVariation(ImageVariationRequest request, File image) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(Fields.N, request.getN().toString()) // integer or null
                .addFormDataPart(Fields.SIZE, request.getSize()) // string or null
                .addFormDataPart(Fields.RESPONSE_FORMAT, request.getResponseFormat()) // string or null
                .addFormDataPart(Fields.IMAGE, image.getName(), RequestBody.create(image, MediaType.parse(IanaMediaType.IMAGE_ALL)));

        if (request.getModel() != null) {
            builder.addFormDataPart(Fields.MODEL, request.getModel());
        }

        if (request.getUser() != null) {
            builder.addFormDataPart(Fields.USER, request.getUser());
        }

        return super.call(api.createImageVariation(null));
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
    public CompletableFuture<OpenAiList<Model>> listModels() {
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

    // ===============================
    // ASSISTANTS - Threads
    // ===============================

    // ===============================
    // ASSISTANTS - Messages
    // ===============================

    // ===============================
    // ASSISTANTS - Runs
    // ===============================

    // ===============================
    // ASSISTANTS - Run Steps
    // ===============================

    // ===============================
    // ASSISTANTS - Vector Stores
    // ===============================

    public CompletableFuture<VectorStore> createVectorStore(VectorStoreRequest vectorStoreRequest) {
        return super.call(api.createVectorStore(vectorStoreRequest));
    }

    public CompletableFuture<OpenAiList<VectorStore>> listVectorStores() {
        return super.call(api.listVectorStores());
    }

    public CompletableFuture<OpenAiList<VectorStore>> listVectorStores(String order, String before, String after, Integer limit) {
        return super.call(api.listVectorStores(order, before, after, limit));
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

    public CompletableFuture<VectorStoreFile> createVectorStoreFile(String vectorStoreId, VectorStoreRequest request) {
        return super.call(api.createVectorStoreFile(vectorStoreId, request));
    }

    public CompletableFuture<OpenAiList<VectorStoreFile>> listVectorStoreFiles(String vectorStoreId) {
        return super.call(api.listVectorStoreFiles(vectorStoreId));
    }

    public CompletableFuture<OpenAiList<VectorStoreFile>> listVectorStoreFiles(
            String vectorStoreId,
            Integer limit,
            String order,
            String before,
            String after,
            String filter
    ) {
        return super.call(api.listVectorStoreFiles(vectorStoreId, limit, order, before, after, filter));
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

    public CompletableFuture<OpenAiList<VectorStoreFileBatch>> listVectorStoreFileBatch(String vectorStoreId, String batchId) {
        return super.call(api.listVectorStoreFileBatch(vectorStoreId, batchId));
    }

    public CompletableFuture<OpenAiList<VectorStoreFileBatch>> listVectorStoreFileBatch(String vectorStoreId, String batchId, Integer limit, String order, String before, String after, String filter) {
        return super.call(api.listVectorStoreFileBatch(vectorStoreId, batchId, limit, order, before, after, filter));
    }

    // ===============================
    // ASSISTANTS - Streaming
    // ===============================
}
