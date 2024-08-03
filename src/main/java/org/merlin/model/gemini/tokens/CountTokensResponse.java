package org.merlin.model.gemini.tokens;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/tokens#response-body">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountTokensResponse {
    private Integer totalTokens;
}
