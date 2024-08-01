package org.haifan.merlin.model.gemini;

/**
 * <a href="https://ai.google.dev/api/generate-content#blockreason">...</a>
 */
public enum BlockReason {
    BLOCK_REASON_UNSPECIFIED,
    SAFETY,
    OTHER,
    BLOCKLIST,
    PROHIBITED_CONTENT,
}
