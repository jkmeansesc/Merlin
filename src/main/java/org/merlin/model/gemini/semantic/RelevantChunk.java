package org.merlin.model.gemini.semantic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/semantic-retrieval/corpora#relevantchunk">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelevantChunk {
    private Double chunkRelevanceScore;
    private Chunk chunk;
}
