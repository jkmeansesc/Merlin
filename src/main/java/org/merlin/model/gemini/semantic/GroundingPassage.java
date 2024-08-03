package org.merlin.model.gemini.semantic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.merlin.model.gemini.caching.Content;

/**
 * <a href="https://ai.google.dev/api/semantic-retrieval/question-answering#groundingpassage">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroundingPassage {
    private String id;
    private Content content;
}
