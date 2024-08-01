package org.haifan.merlin.model.gemini.generatingcontent;

import lombok.*;

/**
 * Safety setting, affecting the safety-blocking behavior.
 * Passing a safety setting for a category changes the allowed probability that content is blocked.
 * <a href="https://ai.google.dev/api/rest/v1/SafetySetting#HarmBlockThreshold">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SafetySetting {
    private HarmCategory category;
    private HarmBlockThreshold threshold;
}
