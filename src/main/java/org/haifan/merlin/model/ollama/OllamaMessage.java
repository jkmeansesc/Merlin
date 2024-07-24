package org.haifan.merlin.model.ollama;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OllamaMessage {
    private String role;
    private String content;
    private List<String> images;
}
