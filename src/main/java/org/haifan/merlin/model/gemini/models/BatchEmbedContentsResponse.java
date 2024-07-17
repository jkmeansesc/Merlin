package org.haifan.merlin.model.gemini.models;

import lombok.Data;
import org.haifan.merlin.model.gemini.ContentEmbedding;

import java.util.List;

@Data
public class BatchEmbedContentsResponse {
    private List<ContentEmbedding> embeddings;
}
