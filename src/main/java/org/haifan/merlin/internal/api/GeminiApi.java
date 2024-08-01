package org.haifan.merlin.internal.api;

import okhttp3.ResponseBody;
import org.haifan.merlin.model.gemini.*;
import org.haifan.merlin.model.gemini.embeddings.EmbedContentRequest;
import org.haifan.merlin.model.gemini.generatingcontent.GenerateContentRequest;
import org.haifan.merlin.model.gemini.generatingcontent.GenerateContentResponse;
import org.haifan.merlin.model.gemini.models.Model;
import org.haifan.merlin.model.gemini.models.ListModel;
import org.haifan.merlin.model.gemini.tokens.CountTokensRequest;
import org.haifan.merlin.model.gemini.tokens.CountTokensResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface GeminiApi {

    // ===============================
    // Models
    // ===============================

    @GET("/v1beta/{name}")
    Call<Model> getModel(@Path(value = "name", encoded = true) String name);

    @GET("/v1beta/models")
    Call<ListModel> listModels();

    @GET("/v1beta/models")
    Call<ListModel> listModels(@Query("pageSize") Integer pageSize, @Query("pageToken") String pageToken);

    // ===============================
    // Generating Content
    // ===============================

    @POST("/v1beta/{model}:generateContent")
    Call<GenerateContentResponse> generateContent(@Path(value = "model", encoded = true) String model, @Body GenerateContentRequest request);

    @POST("/v1beta/{model}:streamGenerateContent")
    @Streaming
    Call<ResponseBody> streamGenerateContent(@Path(value = "model", encoded = true) String model, @Body GenerateContentRequest request);

    // ===============================
    // Generating Content
    // ===============================

    @POST("/v1beta/{model}:batchEmbedContents")
    Call<BatchEmbedContentsResponse> batchEmbedContents(@Path("model") String model, @Body BatchEmbedContentsRequest request);

    @POST("/v1beta/{model}:countTokens")
    Call<CountTokensResponse> countTokens(@Path("model") String model, @Body CountTokensRequest request);

    @POST("/v1beta/{model=models/*}:embedContent")
    Call<EmbedContentResponse> embedContent(@Path("model") String model, @Body EmbedContentRequest request);

    @POST("/v1beta/{name}:cancel")
    Call<Void> cancelTunedModelOperation(@Path("name") String name);
}
