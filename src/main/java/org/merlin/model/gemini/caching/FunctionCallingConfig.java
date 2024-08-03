package org.merlin.model.gemini.caching;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/caching#functioncallingconfig">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FunctionCallingConfig {
    private Mode mode;
    private List<String> allowedFunctionNames;
}
