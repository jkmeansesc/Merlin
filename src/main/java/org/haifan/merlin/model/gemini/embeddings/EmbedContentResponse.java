package org.haifan.merlin.model.gemini.embeddings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/embeddings#response-body">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmbedContentResponse {
    private ContentEmbedding embedding;
}
