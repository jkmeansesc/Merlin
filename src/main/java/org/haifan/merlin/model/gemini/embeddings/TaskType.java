package org.haifan.merlin.model.gemini.embeddings;

/**
 * Type of task for which the embedding will be used.
 * <a href="https://ai.google.dev/api/rest/v1/TaskType">...</a>
 */
public enum TaskType {
    TASK_TYPE_UNSPECIFIED,
    RETRIEVAL_QUERY,
    RETRIEVAL_DOCUMENT,
    SEMANTIC_SIMILARITY,
    CLASSIFICATION,
    CLUSTERING,
    QUESTION_ANSWERING,
    FACT_VERIFICATION,
}