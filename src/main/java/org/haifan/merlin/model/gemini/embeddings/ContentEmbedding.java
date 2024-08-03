package org.haifan.merlin.model.gemini.embeddings;

import lombok.*;

import java.util.List;

/**
 * A list of floats representing an embedding.
 * <a href="https://ai.google.dev/api/rest/v1/ContentEmbedding">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentEmbedding {
    private List<Double> values;
}
