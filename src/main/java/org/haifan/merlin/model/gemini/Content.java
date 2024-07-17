package org.haifan.merlin.model.gemini;

import lombok.Data;

import java.util.List;

/**
 * The base structured datatype containing multi-part content of a message.
 * <a href="https://ai.google.dev/api/rest/v1/Content">...</a>
 */
@Data
public class Content {
    private List<Part> parts;
    private String role;

    @Data
    public static class Part {
        private String text;
        private Blob inlineData;

        @Data
        public static class Blob {
            private String mimeType;
            private String data;
        }
    }
}
