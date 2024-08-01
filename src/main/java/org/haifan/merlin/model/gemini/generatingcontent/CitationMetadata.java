package org.haifan.merlin.model.gemini.generatingcontent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/generate-content#citationmetadata">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitationMetadata {
    private List<CitationSource> citationSources;
}
