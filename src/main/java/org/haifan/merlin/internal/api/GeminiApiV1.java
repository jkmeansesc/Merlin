package org.haifan.merlin.internal.api;

import okhttp3.ResponseBody;
import org.haifan.merlin.model.gemini.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface GeminiApiV1 {

    // ===============================
    // Google Gemini v1
    // ===============================

    @GET("/v1/{name}")
    Call<Model> getModel(@Path(value = "name", encoded = true) String name);

    @GET("/v1/models")
    Call<ModelList> listModels();

    @GET("/v1/models")
    Call<ModelList> listModels(@Query("pageSize") Integer pageSize, @Query("pageToken") String pageToken);

    @POST("/v1/{model}:batchEmbedContents")
    Call<BatchEmbedContentsResponse> batchEmbedContents(@Path("model") String model, @Body BatchEmbedContentsRequest request);

    @POST("/v1/{model}:countTokens")
    Call<CountTokensResponse> countTokens(@Path("model") String model, @Body CountTokensRequest request);

    @POST("/v1/{model=models/*}:embedContent")
    Call<EmbedContentResponse> embedContent(@Path("model") String model, @Body EmbedContentRequest request);

    @POST("/v1/{model}:generateContent")
    Call<GenerateContentResponse> generateContent(@Path("model") String model, @Body GenerateContentRequest request);

    @POST("/v1/{model}:streamGenerateContent")
    @Streaming
    Call<ResponseBody> streamGenerateContent(@Path("model") String model, @Body GenerateContentRequest request);

    @GET("/v1/{name}")
    Call<ListOperationsResponse> listOperations(@Path("name") String name);

    @GET("/v1/{name}")
    Call<ListOperationsResponse> listOperations(@Path("name") String name, @QueryMap Map<String, Object> queryMap);

    @DELETE("/v1/{name}")
    Call<Void> deleteOperation(@Path("name") String name);

    @POST("/v1/{name}:cancel")
    Call<Void> cancelTunedModelOperation(@Path("name") String name);

    @GET("/v1/{name}")
    Call<Operation> getTunedModelOperation(@Path("name") String name);

    @GET("/v1/{name}/operations")
    Call<ListOperationsResponse> listTunedModelsOperations(@Path("name") String name);

    @GET("/v1/{name}/operations")
    Call<ListOperationsResponse> listTunedModelsOperations(@Path("name") String name, @QueryMap Map<String, Object> queryMap);
}
