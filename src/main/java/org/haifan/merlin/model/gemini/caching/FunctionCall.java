package org.haifan.merlin.model.gemini.caching;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * A predicted FunctionCall returned from the model that contains a string representing the FunctionDeclaration.name with the arguments and their values.
 * <a href="https://ai.google.dev/api/caching#functioncall">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FunctionCall {
    private String name;
    private Map<String, Object> args;
}
