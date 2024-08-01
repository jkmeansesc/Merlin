package org.haifan.merlin.model.gemini.semantic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/semantic-retrieval/corpora#condition">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Condition {
    private Operator operation;
    private String stringValue;
    private Double numericValue;
}
