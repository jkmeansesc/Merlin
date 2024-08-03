package org.merlin.model.gemini;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/caching#usagemetadata">...</a>
 * <a href="https://ai.google.dev/api/generate-content#usagemetadata">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageMetadata {
    private Integer promptTokenCount;
    private Integer cachedContentTokenCount;
    private Integer candidatesTokenCount;
    private Integer totalTokenCount;
}
