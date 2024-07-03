package org.haifan.merlin.api;

import org.haifan.merlin.model.openai.OpenAiResponse;
import org.haifan.merlin.model.openai.models.Model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OpenAiApi {
    @GET("/v1/models")
    Call<OpenAiResponse<Model>> listModels();

    @GET("/v1/models/{model}")
    Call<Model> getModel(@Path("model") String model);
}
