package org.merlin.model.gemini.caching;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/caching#toolconfig">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolConfig {
    private FunctionCallingConfig functionCallingConfig;
}
