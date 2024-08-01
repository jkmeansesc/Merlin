package org.haifan.merlin.model.gemini.generatingcontent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/generate-content#safetyrating">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SafetyRating {
    private HarmCategory category;
    private HarmProbability probability;
    private Boolean blocked;
}
