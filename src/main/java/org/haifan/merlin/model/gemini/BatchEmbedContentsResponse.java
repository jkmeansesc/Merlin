package org.haifan.merlin.model.gemini;

import lombok.Data;

import java.util.List;

@Data
public class BatchEmbedContentsResponse {
    private List<ContentEmbedding> embeddings;
}
