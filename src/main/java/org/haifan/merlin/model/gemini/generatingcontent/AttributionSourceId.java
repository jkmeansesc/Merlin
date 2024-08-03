package org.haifan.merlin.model.gemini.generatingcontent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/generate-content#attributionsourceid">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributionSourceId {
    private GroundingPassageId groundingPassage;
    private SemanticRetrieverChunk semanticRetrieverChunk;
}