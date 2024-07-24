package org.haifan.merlin.api;

import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.haifan.merlin.model.ollama.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface OllamaApi {

    @POST("/api/generate")
    @Streaming
    Call<ResponseBody> streamCompletion(@Body OllamaCompletionRequest request);

    @POST("/api/generate")
    Call<OllamaCompletion> createCompletion(@Body OllamaCompletionRequest request);

    @POST("/api/chat")
    @Streaming
    Call<ResponseBody> streamChatCompletion(@Body OllamaCompletionRequest request);

    @POST("/api/chat")
    Call<OllamaCompletion> createChatCompletion(@Body OllamaCompletionRequest request);

    @POST("/api/create")
    @Streaming
    Call<ResponseBody> createModelStream(@Body OllamaCompletionRequest request);

    @POST("/api/create")
    Call<OllamaStatus> createModel(@Body OllamaCompletionRequest request);

    @HEAD("api/blobs/{digest}")
    Call<Void> checkBlob(@Path("digest") String digest);

    @POST("api/blobs/{digest}")
    Call<Void> createBlob(@Path("digest") String digest, @Body RequestBody request);

    @GET("/api/tags")
    Call<OllamaModelList> listModels();

    @POST("/api/show")
    Call<OllamaModel> showModelInfo(@Body OllamaCompletionRequest request);

    @POST("/api/copy")
    Call<Void> copyModel(@Body OllamaCompletionRequest request);

    @DELETE("/api/delete")
    Call<Void> deleteModel(@Body OllamaCompletionRequest request);

    @POST("/api/pull")
    Call<ResponseBody> pullModelStream(@Body OllamaCompletionRequest request);

    @POST("/api/pull")
    Call<OllamaStatus> pullModel(@Body OllamaCompletionRequest request);

    @POST("/api/push")
    Call<ResponseBody> pushModelStream(@Body OllamaCompletionRequest request);

    @POST("/api/push")
    Call<OllamaStatus> pushModel(@Body OllamaCompletionRequest request);

    @POST("/api/embeddings")
    Call<OllamaEmbedding> createEmbedding(@Body OllamaCompletionRequest request);

    @GET("/api/ps")
    Call<OllamaModelList> listRunning();
}