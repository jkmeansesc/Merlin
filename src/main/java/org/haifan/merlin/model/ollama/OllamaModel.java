package org.haifan.merlin.model.ollama;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OllamaModel {
    private String name;
    @JsonProperty("modified_at")
    private String modifiedAt;
    private Long size;
    private String digest;
    private Details details;
    @JsonProperty("model_info")
    private Map<String, Object> modelInfo;

    @Data
    public static class Details {
        private String format;
        private String family;
        private List<String> families;
        @JsonProperty("parameter_size")
        private String parameterSize;
        @JsonProperty("quantization_level")
        private String quantizationLevel;
    }
}
