package org.merlin.model.gemini.semantic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/semantic-retrieval/chunks#createchunkrequest">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateChunkRequest {
    private String parent;
    private Chunk chunk;
}