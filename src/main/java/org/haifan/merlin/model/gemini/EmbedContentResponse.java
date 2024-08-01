package org.haifan.merlin.model.gemini;

import lombok.Data;
import org.haifan.merlin.model.gemini.embeddings.ContentEmbedding;

@Data
public class EmbedContentResponse {
    private ContentEmbedding embedding;
}
