package org.haifan.merlin.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.stubbing.Answer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
            Response<T> response;
            if (responseType == Void.class) {
                // For Void responses, don't deserialize anything
                response = Response.success((T) null);
            } else {
                // This is where the deserialization happens.
                // This simulates what happens when the code receives JSON from the server and deserializes it into the model objects.
                T responseBody = mapper.readValue(jsonResponse, responseType);
                response = Response.success(responseBody);
            }
            // Immediately calls the onResponse method of the callback, simulating a successful response from the server.
            callback.onResponse(call, response);
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

    /**
     * Read file content into a String.
     */
    public static String read(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get("src/test/resources/" + filePath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calculate the SHA-256 checksum of a file.
     */
    public static String getSHA256(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192]; // Read in 8KB chunks
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }
        byte[] hash = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            // Convert each byte to a 2-digit hexadecimal value
            String hex = Integer.toHexString(0xff & b);
            // Pad with leading zero if necessary
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Gets a File object from a path in the test resources folder.
     */
    public static File getFile(String path) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resourceUrl = classLoader.getResource(path);
        if (resourceUrl == null) {
            throw new IOException("Resource not found: " + path);
        }
        return new File(resourceUrl.getFile());
    }
}
