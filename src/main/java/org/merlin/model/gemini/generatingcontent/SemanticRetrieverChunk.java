package org.merlin.model.gemini.generatingcontent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/generate-content#semanticretrieverchunk">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemanticRetrieverChunk {
    private String source;
    private String chunk;
}
