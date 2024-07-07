package org.haifan.merlin.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.haifan.merlin.constants.Fields;
import org.haifan.merlin.model.openai.DeletionStatus;
import org.haifan.merlin.model.openai.audio.CreateSpeechRequest;
import org.haifan.merlin.model.openai.audio.Transcription;
import org.haifan.merlin.model.openai.audio.Translation;
import org.haifan.merlin.model.openai.files.File;
import org.haifan.merlin.model.openai.files.FileResponse;
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
    // ENDPOINTS - Audio
    // ===============================

    @POST("/v1/audio/speech")
    Call<ResponseBody> createSpeech(@Body CreateSpeechRequest requestBody);

    @POST("/v1/audio/transcriptions")
    Call<Transcription> createTranscription(@Body RequestBody requestBody);

    @POST("/v1/audio/translations")
    Call<Translation> createTranslation(@Body RequestBody requestBody);

    // ===============================
    // ENDPOINTS - Files
    // ===============================

    @POST("/v1/files")
    Call<File> uploadFile(@Body RequestBody requestBody);

    @GET("/v1/files")
    Call<FileResponse> listFiles();

    @GET("/v1/files/{file_id}")
    Call<File> retrieveFile(@Path(Fields.FILE_ID) String fileId);

    @DELETE("/v1/files/{file_id}")
    Call<DeletionStatus> deleteFile(@Path(Fields.FILE_ID) String fileId);

    @Streaming
    @GET("/v1/files/{file_id}/content")
    Call<ResponseBody> retrieveFileContent(@Path(Fields.FILE_ID) String fileId);

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
    Call<Model> retrieveModel(@Path(Fields.MODEL) String model);

    @DELETE("/v1/models/{model}")
    Call<DeletionStatus> deleteAFineTunedModel(@Path(Fields.MODEL) String model);

    // ===============================
    // ENDPOINTS - Moderation
    // ===============================
    @POST("v1/moderations")
    Call<ModerationResponse> createModeration(@Body ModerationRequest moderationRequest);

}
