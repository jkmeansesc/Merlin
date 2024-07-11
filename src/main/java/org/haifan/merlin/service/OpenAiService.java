package org.haifan.merlin.service;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import org.haifan.merlin.api.OpenAiApi;
import org.haifan.merlin.config.LlmConfig;
import org.haifan.merlin.config.OpenAiConfig;
import org.haifan.merlin.constants.Fields;
import org.haifan.merlin.model.openai.DeletionStatus;
import org.haifan.merlin.model.openai.audio.*;
import org.haifan.merlin.model.openai.files.FileResponse;
import org.haifan.merlin.model.openai.images.ImageEditRequest;
import org.haifan.merlin.model.openai.images.ImageRequest;
import org.haifan.merlin.model.openai.images.ImageVariationRequest;
import org.haifan.merlin.model.openai.images.ImageResponse;
import org.haifan.merlin.model.openai.models.Model;
import org.haifan.merlin.model.openai.models.ModelResponse;
import org.haifan.merlin.model.openai.moderations.ModerationResponse;
import org.haifan.merlin.model.openai.moderations.ModerationRequest;
import org.haifan.merlin.constants.IanaMediaType;
import org.haifan.merlin.interceptors.OpenAiInterceptor;
import org.haifan.merlin.utils.FileParser;

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

    // ===============================
    // ENDPOINTS - Files
    // ===============================

    public CompletableFuture<org.haifan.merlin.model.openai.files.File> uploadFile(String purpose, String filePath) {
        java.io.File file = new java.io.File(filePath);
        return uploadFile(purpose, file);
    }

    public CompletableFuture<org.haifan.merlin.model.openai.files.File> uploadFile(String purpose, File file) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(Fields.PURPOSE, purpose)
                .addFormDataPart(Fields.FILE, file.getName(), FileParser.parseFile(file));

        return super.call(api.uploadFile(builder.build()));
    }

    public CompletableFuture<FileResponse> listFiles() {
        return super.call(api.listFiles());
    }

    public CompletableFuture<org.haifan.merlin.model.openai.files.File> retrieveFile(String fileId) {
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

    public CompletableFuture<ImageResponse> createImage(ImageRequest imageRequest) {
        return super.call(api.createImage(imageRequest));
    }


    public CompletableFuture<ImageResponse> createImageEdit(ImageEditRequest request, String imagePath, String maskPath) {
        File image = new File(imagePath);
        File mask = maskPath != null ? new File(maskPath) : null;
        return createImageEdit(request, image, mask);
    }

    private CompletableFuture<ImageResponse> createImageEdit(ImageEditRequest request, File image, File mask) {
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

    public CompletableFuture<ImageResponse> createImageVariation(ImageVariationRequest request, String imagePath) {
        java.io.File image = new java.io.File(imagePath);
        return createImageVariation(request, image);
    }

    public CompletableFuture<ImageResponse> createImageVariation(ImageVariationRequest request, File image) {
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
     * @see ModelResponse
     */
    public CompletableFuture<ModelResponse> listModels() {
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

    public CompletableFuture<ModerationResponse> createModeration(ModerationRequest moderationRequest) {
        return super.call(api.createModeration(moderationRequest));
    }

}
