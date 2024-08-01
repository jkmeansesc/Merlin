package org.haifan.merlin.model.gemini.generatingcontent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.haifan.merlin.model.gemini.caching.Content;

/**
 * <a href="https://ai.google.dev/api/generate-content#groundingattribution">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroundingAttribution {
    private AttributionSourceId sourceId;
    private Content content;
}
