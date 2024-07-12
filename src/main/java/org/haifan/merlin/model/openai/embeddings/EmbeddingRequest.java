package org.haifan.merlin.model.openai.embeddings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.haifan.merlin.utils.InputDeserializer;

/**
 * The request model object for creating embeddings for OpenAI.
 * <a href="https://platform.openai.com/docs/api-reference/embeddings/create">...</a>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmbeddingRequest {

    @NonNull
    @JsonDeserialize(using = InputDeserializer.class)
    private Object input;

    @NonNull
    private String model;

    @JsonProperty("encoding_format")
    private String encodingFormat;

    private Integer dimensions;

    private String user;

    public static class EmbeddingRequestBuilder {
        public EmbeddingRequestBuilder input(String input) {
            if (input == null || input.isEmpty()) {
                throw new IllegalArgumentException("Input string cannot be null or empty");
            }
            this.input = input;
            return this;
        }

        public EmbeddingRequestBuilder input(String[] input) {
            return input(java.util.Arrays.asList(input));
        }

        public EmbeddingRequestBuilder input(Integer[] input) {
            return input(java.util.Arrays.asList(input));
        }

        public EmbeddingRequestBuilder input(java.util.List<?> input) {
            if (input == null || input.isEmpty() || input.size() > 2048) {
                throw new IllegalArgumentException("Input list must not be null, must not be empty, and must not exceed 2048 elements");
            }
            this.input = input;
            return this;
        }
    }
}