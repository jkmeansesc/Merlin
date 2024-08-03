package org.haifan.merlin.model.openai.endpoints.embeddings;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * <a href="https://platform.openai.com/docs/api-reference/embeddings/create">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddingRequest {

    @NonNull
    private Input input;

    @NonNull
    private String model;

    @JsonProperty("encoding_format")
    private String encodingFormat;

    private Integer dimensions;

    private String user;

    @SuppressWarnings("unused")
    public static class EmbeddingRequestBuilder {
        public EmbeddingRequestBuilder input(String input) {
            this.input = new Input(input);
            return this;
        }

        public EmbeddingRequestBuilder input(String[] input) {
            this.input = new Input(input);
            return this;
        }

        public EmbeddingRequestBuilder input(Integer[] input) {
            this.input = new Input(input);
            return this;
        }

        public EmbeddingRequestBuilder input(List<?> input) {
            this.input = new Input(input);
            return this;
        }
    }

    @Data
    public static class Input {
        private String inputStr;
        private List<?> inputList;

        public Input(String input) {
            if (input == null || input.isEmpty()) {
                throw new IllegalArgumentException("Input string cannot be null or empty");
            }
            this.inputStr = input;
        }

        public Input(String[] input) {
            this(java.util.Arrays.asList(input));
        }

        public Input(Integer[] input) {
            this(java.util.Arrays.asList(input));
        }

        public Input(java.util.List<?> input) {
            if (input == null || input.isEmpty() || input.size() > 2048) {
                throw new IllegalArgumentException("Input list must not be null, must not be empty, and must not exceed 2048 elements");
            }
            this.inputList = input;
        }
    }
}