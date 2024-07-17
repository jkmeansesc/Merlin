package org.haifan.merlin.model.gemini;

import lombok.Data;

/**
 * Safety setting, affecting the safety-blocking behavior.
 * Passing a safety setting for a category changes the allowed probability that content is blocked.
 * <a href="https://ai.google.dev/api/rest/v1/SafetySetting#HarmBlockThreshold">...</a>
 */
@Data
public class SafetySetting {
    private HarmCategory category;
    private HarmBlockThreshold threshold;

    public enum HarmBlockThreshold {
        HARM_BLOCK_THRESHOLD_UNSPECIFIED,
        BLOCK_LOW_AND_ABOVE,
        BLOCK_MEDIUM_AND_ABOVE,
        BLOCK_ONLY_HIGH,
        BLOCK_NONE,
    }
}
