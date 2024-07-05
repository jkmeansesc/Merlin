package org.haifan.merlin.service;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.MultipartBody;
import org.haifan.merlin.api.OpenAiApi;
import org.haifan.merlin.config.LlmConfig;
import org.haifan.merlin.config.OpenAiConfig;
import org.haifan.merlin.model.openai.DeletionStatus;
import org.haifan.merlin.model.openai.images.CreateImageEditRequest;
import org.haifan.merlin.model.openai.images.CreateImageRequest;
import org.haifan.merlin.model.openai.images.CreateImageVariationRequest;
import org.haifan.merlin.model.openai.images.ImageResponse;
import org.haifan.merlin.model.openai.models.Model;
import org.haifan.merlin.model.openai.models.ModelResponse;
import org.haifan.merlin.model.openai.moderations.ModerationResponse;
import org.haifan.merlin.model.openai.moderations.ModerationRequest;
import org.haifan.merlin.utils.Constants;

import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * TODO: add javadoc
 */
public class OpenAiService extends LlmService {

    private final OpenAiApi api;

    public OpenAiService() {
        super(new OpenAiConfig());
        this.api = super.retrofit.create(OpenAiApi.class);
    }

    public OpenAiService(String apiKey) {
        super(new OpenAiConfig(apiKey));
        this.api = super.retrofit.create(OpenAiApi.class);
    }

    @Override
    public LlmConfig getConfig() {
        return super.llmConfig;
    }

    // ===============================
    // ENDPOINTS - Images
    // ===============================

    public CompletableFuture<ImageResponse> createImage(CreateImageRequest createImageRequest) {
        return super.executeCall(api.createImage(createImageRequest));
    }


    public CompletableFuture<ImageResponse> createImageEdit(CreateImageEditRequest request, String imagePath, String maskPath) {
        File image = new File(imagePath);
        File mask = maskPath != null ? new File(maskPath) : null;
        return createImageEdit(request, image, mask);
    }

    public CompletableFuture<ImageResponse> createImageEdit(CreateImageEditRequest request, File image, File mask) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("n", request.getN().toString()) // integer or null
                .addFormDataPart("size", request.getSize()) // string or null
                .addFormDataPart("response_format", request.getResponseFormat()) //string or null
                .addFormDataPart("prompt", request.getPrompt())
                .addFormDataPart("image", image.getName(), RequestBody.create(image, MediaType.parse(Constants.ALL_IMAGE)));

        if (mask != null) {
            builder.addFormDataPart("mask", mask.getName(), RequestBody.create(mask, MediaType.parse(Constants.ALL_IMAGE)));
        }
        if (request.getModel() != null) {
            builder.addFormDataPart("model", request.getModel());
        }
        if (request.getUser() != null) {
            builder.addFormDataPart("user", request.getUser());
        }

        return super.executeCall(api.createImageEdit(builder.build()));
    }

    public CompletableFuture<ImageResponse> createImageVariation(CreateImageVariationRequest request, String imagePath) {
        java.io.File image = new java.io.File(imagePath);
        return createImageVariation(request, image);
    }

    public CompletableFuture<ImageResponse> createImageVariation(CreateImageVariationRequest request, File image) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("n", request.getN().toString()) // integer or null
                .addFormDataPart("size", request.getSize()) // string or null
                .addFormDataPart("response_format", request.getResponseFormat()) // string or null
                .addFormDataPart("image", image.getName(), RequestBody.create(image, MediaType.parse(Constants.ALL_IMAGE)));

        if (request.getModel() != null) {
            builder.addFormDataPart("model", request.getModel());
        }

        if (request.getUser() != null) {
            builder.addFormDataPart("user", request.getUser());
        }

        return super.executeCall(api.createImageVariation(null));
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
        return super.executeCall(api.listModels());
    }

    /**
     * Retrieves a model instance, providing basic information about the model such as the owner and permission.
     *
     * @param model The ID of the model to use for this request
     * @return The model object matching the specified ID.
     * @see Model
     */
    public CompletableFuture<Model> retrieveModel(String model) {
        return super.executeCall(api.retrieveModel(model));
    }

    /**
     * Delete a fine-tuned model. You must have the Owner role in your organization to delete a model.
     *
     * @param model the model to delete
     * @return deletion status
     * @see DeletionStatus
     */
    public CompletableFuture<DeletionStatus> deleteAFineTunedModel(String model) {
        return super.executeCall(api.deleteAFineTunedModel(model));
    }

    // ===============================
    // ENDPOINTS - Moderation
    // ===============================

    public CompletableFuture<ModerationResponse> createModeration(ModerationRequest moderationRequest) {
        return super.executeCall(api.createModeration(moderationRequest));
    }

}
