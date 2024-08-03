package org.merlin.internal.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.merlin.model.gemini.GeminiData;
import org.merlin.model.gemini.caching.CachedContent;
import org.merlin.model.gemini.embeddings.BatchEmbedContentsRequest;
import org.merlin.model.gemini.embeddings.BatchEmbedContentsResponse;
import org.merlin.model.gemini.embeddings.EmbedContentRequest;
import org.merlin.model.gemini.embeddings.EmbedContentResponse;
import org.merlin.model.gemini.files.GeminiFile;
import org.merlin.model.gemini.files.UploadMediaRequest;
import org.merlin.model.gemini.files.UploadMediaResponse;
import org.merlin.model.gemini.generatingcontent.GenerateContentRequest;
import org.merlin.model.gemini.generatingcontent.GenerateContentResponse;
import org.merlin.model.gemini.models.Model;
import org.merlin.model.gemini.tokens.CountTokensRequest;
import org.merlin.model.gemini.tokens.CountTokensResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface GeminiApi {

    // ===============================
    // Models
    // ===============================

    @GET("/v1beta/{name}")
    Call<Model> getModel(@Path(value = "name", encoded = true) String name);

    @GET("/v1beta/models")
    Call<GeminiData<Model>> listModels();

    @GET("/v1beta/models")
    Call<GeminiData<Model>> listModels(@Query("pageSize") Integer pageSize, @Query("pageToken") String pageToken);

    // ===============================
    // Generating Content
    // ===============================

    @POST("/v1beta/{model}:generateContent")
    Call<GenerateContentResponse> generateContent(@Path(value = "model", encoded = true) String model, @Body GenerateContentRequest request);

    @POST("/v1beta/{model}:streamGenerateContent?alt=sse")
    @Streaming
    Call<ResponseBody> streamGenerateContent(@Path(value = "model", encoded = true) String model, @Body GenerateContentRequest request);

    // ===============================
    // Tokens
    // ===============================

    @POST("/v1beta/{model}:countTokens")
    Call<CountTokensResponse> countTokens(@Path(value = "model", encoded = true) String model, @Body CountTokensRequest request);

    // ===============================
    // Files
    // ===============================

    @POST("/upload/v1beta/files")
    Call<Void> startUpload(
            @Header("X-Goog-Upload-Protocol") String protocol,
            @Header("X-Goog-Upload-Command") String command,
            @Header("X-Goog-Upload-Header-Content-Length") Long contentLength,
            @Header("X-Goog-Upload-Header-Content-Type") String contentType,
            @Body UploadMediaRequest request
    );

    @POST
    Call<UploadMediaResponse> uploadMedia(@Url String uploadUrl,
                                          @Header("Content-Length") Long contentLength,
                                          @Header("X-Goog-Upload-Offset") Long offset,
                                          @Header("X-Goog-Upload-Command") String command,
                                          @Body RequestBody file);

    @POST("/v1beta/files")
    Call<UploadMediaResponse> uploadMediaMetadata(@Body UploadMediaRequest request);

    @GET("/v1beta/{name}")
    Call<GeminiFile> getFile(@Path(value = "name", encoded = true) String name);

    @GET("/v1beta/files")
    Call<GeminiData<GeminiFile>> listFiles();

    @GET("/v1beta/files")
    Call<GeminiData<GeminiFile>> listFiles(@Query("pageSize") Integer pageSize, @Query("pageToken") String pageToken);

    @DELETE("/v1beta/{name}")
    Call<Void> deleteFile(@Path(value = "name", encoded = true) String name);

    // ===============================
    // Caching
    // ===============================

    @POST("/v1beta/cachedContents")
    Call<CachedContent> createCachedContents(@Body CachedContent request);

    @GET("/v1beta/cachedContents")
    Call<GeminiData<CachedContent>> listCachedContents();

    @GET("/v1beta/cachedContents")
    Call<GeminiData<CachedContent>> listCachedContents(@Query("pageSize") Integer pageSize, @Query("pageToken") String pageToken);

    @GET("/v1beta/{name}")
    Call<CachedContent> getCachedContent(@Path(value = "name", encoded = true) String name);

    @PATCH("/v1beta/{name}")
    Call<CachedContent> updateCachedContent(@Path(value = "name", encoded = true) String name, @Body CachedContent request);

    @PATCH("/v1beta/{name}")
    Call<CachedContent> updateCachedContent(@Path(value = "name", encoded = true) String name, @Body CachedContent request, @Query("updateMask") String updateMask);

    @DELETE("/v1beta/{name}")
    Call<Void> deleteCachedContent(@Path(value = "name", encoded = true) String name);

    // ===============================
    // Embeddings
    // ===============================

    @POST("/v1beta/{model}:embedContent")
    Call<EmbedContentResponse> embedContent(@Path(value = "model", encoded = true) String model, @Body EmbedContentRequest request);

    @POST("/v1beta/{model}:batchEmbedContents")
    Call<BatchEmbedContentsResponse> batchEmbedContents(@Path(value = "model", encoded = true) String model, @Body BatchEmbedContentsRequest request);
}
