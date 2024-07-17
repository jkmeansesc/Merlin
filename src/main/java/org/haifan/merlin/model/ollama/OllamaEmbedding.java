package org.haifan.merlin.model.ollama;

import lombok.Data;

import java.util.List;

@Data
public class OllamaEmbedding {
    private List<Double> embedding;
}
