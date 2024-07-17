package org.haifan.merlin.model.gemini.models;

import lombok.Data;
import org.haifan.merlin.model.gemini.ContentEmbedding;

@Data
public class EmbedContentResponse {
    private ContentEmbedding embedding;
}
