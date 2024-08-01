package org.haifan.merlin.model.gemini.caching;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result of executing the ExecutableCode.
 * <p>
 * Only generated when using the CodeExecution, and always follows a part containing the ExecutableCode.
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
