package org.haifan.merlin.model.gemini.generatingcontent;

/**
 * <a href="https://ai.google.dev/api/generate-content#finishreason">...</a>
 */
public enum FinishReason {
    FINISH_REASON_UNSPECIFIED,
    STOP,
    MAX_TOKENS,
    SAFETY,
    RECITATION,
    LANGUAGE,
    OTHER,
    BLOCKLIST,
    PROHIBITED_CONTENT,
    SPII,
    MALFORMED_FUNCTION_CALL,
}
