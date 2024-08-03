package org.haifan.merlin.model.gemini.caching;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/caching#codeexecutionresult">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeExecutionResult {
    private Outcome outcome;
    private String output;
}
