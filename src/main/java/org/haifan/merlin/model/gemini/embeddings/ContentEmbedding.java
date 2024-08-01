package org.haifan.merlin.model.gemini.embeddings;

import lombok.Data;

import java.util.List;

/**
 * A list of floats representing an embedding.
 * <a href="https://ai.google.dev/api/rest/v1/ContentEmbedding">...</a>
 */
@Data
public class ContentEmbedding {
    private List<Double> values;
}
