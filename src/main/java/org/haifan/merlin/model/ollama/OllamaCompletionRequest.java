package org.haifan.merlin.model.ollama;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OllamaCompletionRequest {
    private String model;
    private String name;
    private Boolean verbose;
    private List<OllamaMessage> messages;
    private String prompt;
    private List<String> images;
    private String format;
    private Options options;
    private String system;
    private String template;
    private String context;
    private Boolean stream;
    private Boolean raw;
    @JsonProperty("keep_alive")
    private Integer keepAlive;
    private String path;
    @JsonProperty("modelfile")
    private String modelFile;
    private String source;
    private String destination;
    private String insecure;
}
