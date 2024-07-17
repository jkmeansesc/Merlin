package org.haifan.merlin.model.ollama;

import lombok.Data;

import java.util.List;

@Data
public class OllamaMessage {
    private String role;
    private String content;
    private List<String> images;
}
