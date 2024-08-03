package org.haifan.merlin.model.gemini.embeddings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/embeddings#response-body_1">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchEmbedContentsResponse {
    private List<ContentEmbedding> embeddings;
}
