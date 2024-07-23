package org.haifan.merlin.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.stubbing.Answer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@SuppressWarnings("unchecked")
public class TestHelper {

    /**
     * Sets up a successful asynchronous response for a Retrofit Call.
     */
    public static <T> void setupSuccessfulAsyncResponseWithJson(Call<T> call, String jsonResponse, Class<T> responseType, ObjectMapper mapper) {
        doAnswer((Answer<Void>) invocation -> {
            Callback<T> callback = invocation.getArgument(0);
            // This is where the deserialization happens.
            // This simulates what happens when the code receives JSON from the server and deserializes it into the model objects.
            T response = mapper.readValue(jsonResponse, responseType);
            // Immediately calls the onResponse method of the callback, simulating a successful response from the server.
            callback.onResponse(call, Response.success(response));
            return null;
        }).when(call).enqueue(any(Callback.class));
    }

    /**
     * Sets up a failed asynchronous response for a Retrofit Call.
     */
    public static <T> void setupFailedAsyncResponse(Call<T> call, Throwable t) {
        doAnswer((Answer<Void>) invocation -> {
            Callback<T> callback = invocation.getArgument(0);
            callback.onFailure(call, t);
            return null;
        }).when(call).enqueue(any(Callback.class));
    }

    public static String read(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get("src/test/resources/" + filePath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
