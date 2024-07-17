package org.haifan.merlin.model.ollama;

import lombok.Data;

@Data
public class OllamaStatus {
    private String status;
    private String digest;
    private Long total;
    private Long completed;
}
