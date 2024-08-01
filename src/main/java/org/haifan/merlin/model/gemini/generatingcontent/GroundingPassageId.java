package org.haifan.merlin.model.gemini.generatingcontent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/generate-content#groundingpassageid">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroundingPassageId {
    private String passageId;
    private Integer partIndex;
}
