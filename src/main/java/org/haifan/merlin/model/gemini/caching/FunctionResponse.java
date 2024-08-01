package org.haifan.merlin.model.gemini.caching;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * The result output from a FunctionCall that contains a string representing the FunctionDeclaration.name and a structured JSON object containing any output from the function is used as context to the model. This should contain the result of aFunctionCall made based on model prediction.
 * <a href="https://ai.google.dev/api/caching#functionresponse">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FunctionResponse {
    private String name;
    private Map<String, Object> response;
}
