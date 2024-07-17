package org.haifan.merlin.model.ollama;

import lombok.Data;

import java.util.List;

@Data
public class OllamaModelList {
    private List<OllamaModel> models;
}
