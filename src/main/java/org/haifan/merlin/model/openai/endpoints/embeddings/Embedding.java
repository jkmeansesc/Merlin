package org.haifan.merlin.model.openai.endpoints.embeddings;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Embedding {

    private Integer index;

    @JsonProperty("embedding")
    private List<Float> embeddingVector;

    private String object;
}
