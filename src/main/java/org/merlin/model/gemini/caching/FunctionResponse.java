package org.merlin.model.gemini.caching;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
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
