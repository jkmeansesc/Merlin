package org.merlin.model.gemini.semantic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/semantic-retrieval/chunks#chunkdata">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChunkData {
    private String stringValue;
}
