package org.merlin.model.ollama;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OllamaModel {
    private String name;
    private String model;
    @JsonProperty("modified_at")
    private String modifiedAt;
    private Long size;
    private String digest;
    private Details details;
    @JsonProperty("model_info")
    private Map<String, Object> modelInfo;
    @JsonProperty("expires_at")
    private String expiresAt;
    @JsonProperty("size_vram")
    private Integer sizeVRam;
    private String modelfile;
    private String parameters;
    private String license;
    private String template;

}
