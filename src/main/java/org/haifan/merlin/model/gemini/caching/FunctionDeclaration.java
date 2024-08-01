package org.haifan.merlin.model.gemini.caching;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/caching#functiondeclaration">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FunctionDeclaration {
    private String name;
    private String description;
    private Schema parameters;
}
