package org.haifan.merlin.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.haifan.merlin.constants.Fields;
import org.haifan.merlin.model.openai.assistants.assistants.Assistant;
import org.haifan.merlin.model.openai.assistants.assistants.AssistantRequest;
import org.haifan.merlin.model.openai.assistants.vectorstores.VectorStore;
import org.haifan.merlin.model.openai.assistants.vectorstores.VectorStoreFile;
import org.haifan.merlin.model.openai.assistants.vectorstores.VectorStoreFileBatch;
import org.haifan.merlin.model.openai.assistants.vectorstores.VectorStoreRequest;
import org.haifan.merlin.model.openai.DeletionStatus;
import org.haifan.merlin.model.openai.OpenAiList;
import org.haifan.merlin.model.openai.endpoints.audio.SpeechRequest;
import org.haifan.merlin.model.openai.endpoints.audio.Transcription;
import org.haifan.merlin.model.openai.endpoints.audio.Translation;
import org.haifan.merlin.model.openai.endpoints.batch.Batch;
import org.haifan.merlin.model.openai.endpoints.batch.BatchRequest;
import org.haifan.merlin.model.openai.endpoints.chat.ChatCompletion;
import org.haifan.merlin.model.openai.endpoints.chat.ChatCompletionRequest;
import org.haifan.merlin.model.openai.endpoints.embeddings.Embedding;
import org.haifan.merlin.model.openai.endpoints.embeddings.EmbeddingRequest;
import org.haifan.merlin.model.openai.endpoints.files.File;
import org.haifan.merlin.model.openai.endpoints.finetune.FineTuningCheckpoint;
import org.haifan.merlin.model.openai.endpoints.finetune.FineTuningEvent;
import org.haifan.merlin.model.openai.endpoints.finetune.FineTuningJob;
import org.haifan.merlin.model.openai.endpoints.finetune.FineTuningJobRequest;
import org.haifan.merlin.model.openai.endpoints.images.ImageRequest;
import org.haifan.merlin.model.openai.endpoints.images.ImageList;
import org.haifan.merlin.model.openai.endpoints.models.Model;

import org.haifan.merlin.model.openai.endpoints.moderations.ModerationList;
import org.haifan.merlin.model.openai.endpoints.moderations.ModerationRequest;
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
    // ENDPOINTS - Batch
    // ===============================

    @POST("/v1/batches")
    Call<Batch> createBatch(@Body BatchRequest request);

    @GET("/v1/batches/{batch_id}")
    Call<Batch> retrieveBatch(@Path("batch_id") String batchId);

    @POST("/v1/batches/{batch_id}/cancel")
    Call<Batch> cancelBatch(@Path("batch_id") String batchId);

    @GET("/v1/batches")
    Call<OpenAiList<Batch>> listBatches();

    @GET("/v1/batches")
    Call<OpenAiList<Batch>> listBatches(@Query("after") String after, @Query("limit") Integer limit);

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
    Call<ImageList> createImage(@Body ImageRequest request);

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
    Call<ModerationList> createModeration(@Body ModerationRequest request);

    // ===============================
    // ASSISTANTS - Assistants
    // ===============================
    @POST("/v1/assistants")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<Assistant> createAssistant(@Body AssistantRequest request);

    @GET("/v1/assistants")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<OpenAiList<Assistant>> listAssistants();

    @GET("/v1/assistants")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<OpenAiList<Assistant>> listAssistants(
            @Query("limit") Integer limit,
            @Query("order") String order,
            @Query("before") String before,
            @Query("after") String after
    );

    @GET("/v1/assistants/{assistant_id}")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<Assistant> retrieveAssistant(@Path("assistant_id") String assistantId);

    @POST("/v1/assistants/{assistant_id}")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<Assistant> modifyAssistant(@Path("assistant_id") String assistantId, @Body AssistantRequest request);

    @DELETE("/v1/assistants/{assistant_id}")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<DeletionStatus> deleteAssistant(@Path("assistant_id") String assistantId);


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

    @POST("/v1/vector_stores")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<VectorStore> createVectorStore(@Body VectorStoreRequest vectorStoreRequest);

    @GET("/v1/vector_stores")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<OpenAiList<VectorStore>> listVectorStores();

    @GET("/v1/vector_stores")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<OpenAiList<VectorStore>> listVectorStores(@Query("order") String order, @Query("before") String before, @Query("after") String after, @Query("limit") Integer limit);

    @GET("/v1/vector_stores/{vector_store_id}")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<VectorStore> retrieveVectorStore(@Path("vector_store_id") String vectorStoreId);

    @POST("/v1/vector_stores/{vector_store_id}")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<VectorStore> modifyVectorStore(@Path("vector_store_id") String vectorStoreId, @Body VectorStoreRequest request);

    @DELETE("/v1/vector_stores/{vector_store_id}")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<DeletionStatus> deleteVectorStore(@Path("vector_store_id") String vectorStoreId);


    // ===============================
    // ASSISTANTS - Vector Stores Files
    // ===============================

    @POST("/v1/vector_stores/{vector_store_id}/files")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<VectorStoreFile> createVectorStoreFile(@Path("vector_store_id") String vectorStoreId, @Body VectorStoreRequest request);

    @GET("/v1/vector_stores/{vector_store_id}/files")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<OpenAiList<VectorStoreFile>> listVectorStoreFiles(@Path("vector_store_id") String vectorStoreId);

    @GET("/v1/vector_stores/{vector_store_id}/files")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<OpenAiList<VectorStoreFile>> listVectorStoreFiles(@Path("vector_store_id") String vectorStoreId, @Query("limit") Integer limit, @Query("order") String order, @Query("before") String before, @Query("after") String after, @Query("filter") String filter);

    @GET("/v1/vector_stores/{vector_store_id}/files/{file_id}")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<VectorStoreFile> retrieveVectorStoreFile(@Path("vector_store_id") String vectorStoreId, @Path("file_id") String fileId);

    @DELETE("/v1/vector_stores/{vector_store_id}/files/{file_id}")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<DeletionStatus> deleteVectorStoreFile(@Path("vector_store_id") String vectorStoreId, @Path("file_id") String fileId);

    // ===============================
    // ASSISTANTS - Vector Stores File Batches
    // ===============================

    @POST("/v1/vector_stores/{vector_store_id}/file_batches")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<VectorStoreFile> createVectorStoreFileBatch(@Path("vector_store_id") String vectorStoreId, @Body VectorStoreRequest request);

    @GET("/v1/vector_stores/{vector_store_id}/file_batches/{batch_id}")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<VectorStoreFileBatch> retrieveVectorStoreFileBatch(@Path("vector_store_id") String vectorStoreId, @Path("batch_id") String batchId);

    @POST("/v1/vector_stores/{vector_store_id}/file_batches/{batch_id}/cancel")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<VectorStoreFileBatch> cancelVectorStoreFileBatch(@Path("vector_store_id") String vectorStoreId, @Path("batch_id") String batchId);

    @GET("/v1/vector_stores/{vector_store_id}/file_batches/{batch_id}/files")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<OpenAiList<VectorStoreFileBatch>> listVectorStoreFileBatch(@Path("vector_store_id") String vectorStoreId, @Path("batch_id") String batchId);

    @GET("/v1/vector_stores/{vector_store_id}/file_batches/{batch_id}/files")
    @Headers("OpenAI-Beta: assistants=v2")
    Call<OpenAiList<VectorStoreFileBatch>> listVectorStoreFileBatch(@Path("vector_store_id") String vectorStoreId, @Path("batch_id") String batchId, @Query("limit") Integer limit, @Query("order") String order, @Query("before") String before, @Query("after") String after, @Query("filter") String filter);

    // ===============================
    // ASSISTANTS - Streaming
    // ===============================
}
