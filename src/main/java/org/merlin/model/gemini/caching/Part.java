package org.merlin.model.gemini.caching;

import lombok.*;

/**
 * <a href="https://ai.google.dev/api/caching#part">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Part {
    private String text;
    private Blob inlineData;
    private FunctionCall functionCall;
    private FunctionResponse functionResponse;
    private FileData fileData;
    private ExecutableCode executableCode;
    private CodeExecutionResult codeExecutionResult;
}
