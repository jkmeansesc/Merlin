package org.merlin.model.gemini;

/**
 * <a href="https://ai.google.dev/api/files#state">...</a>
 * <a href="https://ai.google.dev/api/tuning#state">...</a>
 * <a href="https://ai.google.dev/api/semantic-retrieval/chunks#state">...</a>
 */
public enum State {
    STATE_UNSPECIFIED,
    STATE_PENDING_PROCESSING,
    STATE_ACTIVE,
    STATE_FAILED,
    PROCESSING,
    CREATING,
    ACTIVE,
    FAILED,
}
