package org.merlin.model.gemini.generatingcontent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/generate-content#citationsource">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitationSource {
    private Integer startIndex;
    private Integer endIndex;
    private String uri;
    private String license;
}
