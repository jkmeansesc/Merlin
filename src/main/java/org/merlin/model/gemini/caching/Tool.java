package org.merlin.model.gemini.caching;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/caching#tool">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tool {
    List<FunctionDeclaration> functionDeclarations;
    private CodeExecution codeExecution;
}
