package org.haifan.merlin.api;

import okhttp3.RequestBody;
import org.haifan.merlin.model.openai.DeletionStatus;
import org.haifan.merlin.model.openai.images.CreateImageRequest;
import org.haifan.merlin.model.openai.images.ImageResponse;
import org.haifan.merlin.model.openai.models.Model;

import org.haifan.merlin.model.openai.models.ModelResponse;
import org.haifan.merlin.model.openai.moderations.ModerationResponse;
import org.haifan.merlin.model.openai.moderations.ModerationRequest;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * TODO: add javadoc
 */
public interface OpenAiApi {

    // ===============================
    // ENDPOINTS - Images
    // ===============================

    @POST("v1/images/generations")
    Call<ImageResponse> createImage(@Body CreateImageRequest createImageRequest);

    @POST("/v1/images/edits")
    Call<ImageResponse> createImageEdit(@Body RequestBody requestBody);

    @POST("/v1/images/variations")
    Call<ImageResponse> createImageVariation(@Body RequestBody requestBody);

    // ===============================
    // ENDPOINTS - Models
    // ===============================
    @GET("/v1/models")
    Call<ModelResponse> listModels();

    @GET("/v1/models/{model}")
    Call<Model> retrieveModel(@Path("model") String model);

    @DELETE("/v1/models/{model}")
    Call<DeletionStatus> deleteAFineTunedModel(@Path("model") String model);

    // ===============================
    // ENDPOINTS - Moderation
    // ===============================
    @POST("v1/moderations")
    Call<ModerationResponse> createModeration(@Body ModerationRequest moderationRequest);

}
