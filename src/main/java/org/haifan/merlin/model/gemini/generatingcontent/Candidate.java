package org.haifan.merlin.model.gemini.generatingcontent;

import lombok.*;
import org.haifan.merlin.model.gemini.caching.Content;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/generate-content#candidate">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {
    private Content content;
    private FinishReason finishReason;
    private List<SafetyRating> safetyRatings;
    private CitationMetadata citationMetadata;
    private Integer tokenCount;
    private GroundingAttribution groundingAttributions;
    private Integer index;
}
