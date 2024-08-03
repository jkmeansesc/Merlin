package org.merlin.model.gemini.generatingcontent;

/**
 * <a href="https://ai.google.dev/api/generate-content#harmblockthreshold">...</a>
 */
public enum HarmBlockThreshold {
    HARM_BLOCK_THRESHOLD_UNSPECIFIED,
    BLOCK_LOW_AND_ABOVE,
    BLOCK_MEDIUM_AND_ABOVE,
    BLOCK_ONLY_HIGH,
    BLOCK_NONE,
}
