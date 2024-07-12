package org.haifan.merlin.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.haifan.merlin.constants.Fields;
import org.haifan.merlin.model.openai.DeletionStatus;
import org.haifan.merlin.model.openai.OpenAiList;
import org.haifan.merlin.model.openai.audio.SpeechRequest;
import org.haifan.merlin.model.openai.audio.Transcription;
import org.haifan.merlin.model.openai.audio.Translation;
import org.haifan.merlin.model.openai.chat.ChatCompletion;
import org.haifan.merlin.model.openai.chat.ChatCompletionRequest;
import org.haifan.merlin.model.openai.embeddings.Embedding;
import org.haifan.merlin.model.openai.embeddings.EmbeddingRequest;
import org.haifan.merlin.model.openai.files.File;
import org.haifan.merlin.model.openai.finetune.FineTuningCheckpoint;
import org.haifan.merlin.model.openai.finetune.FineTuningEvent;
import org.haifan.merlin.model.openai.finetune.FineTuningJob;
import org.haifan.merlin.model.openai.finetune.FineTuningJobRequest;
import org.haifan.merlin.model.openai.images.ImageRequest;
import org.haifan.merlin.model.openai.images.ImageList;
import org.haifan.merlin.model.openai.models.Model;

import org.haifan.merlin.model.openai.moderations.ModerationList;
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
    Call<ResponseBody> createSpeech(@Body SpeechRequest requestBody);

    @POST("/v1/audio/transcriptions")
    Call<Transcription> createTranscription(@Body RequestBody requestBody);

    @POST("/v1/audio/translations")
    Call<Translation> createTranslation(@Body RequestBody requestBody);

    // ===============================
    // ENDPOINTS - Chat
    // ===============================

    @POST("/v1/chat/completions")
    Call<ChatCompletion> createChatCompletion(@Body ChatCompletionRequest request);

    @Streaming
    @POST("v1/chat/completions")
    Call<ResponseBody> streamChatCompletion(@Body ChatCompletionRequest request);

    // ===============================
    // ENDPOINTS - Embeddings
    // ===============================

    @POST("/v1/embeddings")
    Call<Embedding> createEmbeddings(@Body EmbeddingRequest request);

    // ===============================
    // ENDPOINTS - Fine-tuning
    // ===============================

    @POST("/v1/fine_tuning/jobs")
    Call<FineTuningJob> createFineTuningJob(@Body FineTuningJobRequest request);

    @GET("/v1/fine_tuning/jobs")
    Call<OpenAiList<FineTuningJob>> listFineTuningJobs();

    @GET("/v1/fine_tuning/jobs")
    Call<OpenAiList<FineTuningJob>> listFineTuningJobs(@Query("after") String after, @Query("limit") Integer limit);

    @GET("v1/fine_tuning/jobs/{fine_tuning_job_id}/events")
    Call<OpenAiList<FineTuningEvent>> listFineTuningEvents(@Path("fine_tuning_job_id") String fineTuningJobId);

    @GET("v1/fine_tuning/jobs/{fine_tuning_job_id}/events")
    Call<OpenAiList<FineTuningEvent>> listFineTuningEvents(@Path("fine_tuning_job_id") String fineTuningJobId, @Query("after") String after, @Query("limit") Integer limit);

    @GET("v1/fine_tuning/jobs/{fine_tuning_job_id}/checkpoints")
    Call<OpenAiList<FineTuningCheckpoint>> listFineTuningCheckpoints(@Path("fine_tuning_job_id") String fineTuningJobId);

    @GET("v1/fine_tuning/jobs/{fine_tuning_job_id}/checkpoints")
    Call<OpenAiList<FineTuningCheckpoint>> listFineTuningCheckpoints(@Path("fine_tuning_job_id") String fineTuningJobId, @Query("after") String after, @Query("limit") Integer limit);

    @GET("/v1/fine_tuning/jobs/{fine_tuning_job_id}")
    Call<FineTuningJob> retrieveFineTuningJob(@Path("fine_tuning_job_id") String fineTuningJobId);

    @POST("/v1/fine_tuning/jobs/{fine_tuning_job_id}/cancel")
    Call<FineTuningJob> cancelFineTuningJob(@Path("fine_tuning_job_id") String fineTuningJobId);

    // ===============================
    // ENDPOINTS - Files
    // ===============================

    // ===============================
    // ENDPOINTS - Files
    // ===============================

    @POST("/v1/files")
    Call<File> uploadFile(@Body RequestBody requestBody);

    @GET("/v1/files")
    Call<OpenAiList<File>> listFiles();

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
    Call<ImageList> createImage(@Body ImageRequest imageRequest);

    @POST("/v1/images/edits")
    Call<ImageList> createImageEdit(@Body RequestBody requestBody);

    @POST("/v1/images/variations")
    Call<ImageList> createImageVariation(@Body RequestBody requestBody);

    // ===============================
    // ENDPOINTS - Models
    // ===============================
    @GET("/v1/models")
    Call<OpenAiList<Model>> listModels();

    @GET("/v1/models/{model}")
    Call<Model> retrieveModel(@Path(Fields.MODEL) String model);

    @DELETE("/v1/models/{model}")
    Call<DeletionStatus> deleteAFineTunedModel(@Path(Fields.MODEL) String model);

    // ===============================
    // ENDPOINTS - Moderation
    // ===============================
    @POST("v1/moderations")
    Call<ModerationList> createModeration(@Body ModerationRequest moderationRequest);

}
