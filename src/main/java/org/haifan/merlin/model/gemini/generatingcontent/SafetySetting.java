package org.haifan.merlin.model.gemini.generatingcontent;

import lombok.*;

/**
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
