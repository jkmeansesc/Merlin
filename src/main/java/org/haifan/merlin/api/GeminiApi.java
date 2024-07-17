package org.haifan.merlin.api;

import okhttp3.ResponseBody;
import org.haifan.merlin.model.gemini.GenerateContentResponse;
import org.haifan.merlin.model.gemini.models.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface GeminiApi {

    // ===============================
    // v1 - models
    // ===============================

    @POST("/v1/{model}:batchEmbedContents")
    Call<BatchEmbedContentsResponse> batchEmbedContents(@Path("model") String model, @Body BatchEmbedContentsRequest request);

    @POST("/v1/{model}:countTokens")
    Call<CountTokensResponse> countTokens(@Path("model") String model, @Body CountTokensRequest request);

    @POST("/v1/{model=models/*}:embedContent")
    Call<EmbedContentResponse> embedContent(@Path("model") String model, @Body EmbedContentRequest request);

    @POST("/v1/{model}:generateContent")
    Call<GenerateContentResponse> generateContent(@Path("model") String model, @Body GenerateContentRequest request);

    @GET("/v1/{name}")
    Call<Model> getModel(@Path("name") String name);

    @GET("/v1/models")
    Call<ModelList> listModels();

    @GET("/v1/models")
    Call<ModelList> listModels(@Query("pageSize") Integer pageSize, @Query("pageToken") String pageToken);

    @POST("/v1/{model}:streamGenerateContent")
    @Streaming
    Call<ResponseBody> streamGenerateContent(@Path("model") String model, @Body GenerateContentRequest request);
}
