package org.merlin.model.gemini.semantic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.merlin.model.gemini.State;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/semantic-retrieval/chunks#resource:-chunk">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chunk {
    private String name;
    private ChunkData data;
    private List<CustomMetadata> customMetadata;
    private String createTime;
    private String updateTime;
    private State state;
}
