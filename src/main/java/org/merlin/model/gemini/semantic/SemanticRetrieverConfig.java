package org.merlin.model.gemini.semantic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.merlin.model.gemini.caching.Content;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/semantic-retrieval/question-answering#semanticretrieverconfig">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemanticRetrieverConfig {
    private String source;
    private Content query;
    private List<MetadataFilter> metadataFilters;
    private Integer maxChunksCount;
    private Double minimumRelevanceScore;
}
