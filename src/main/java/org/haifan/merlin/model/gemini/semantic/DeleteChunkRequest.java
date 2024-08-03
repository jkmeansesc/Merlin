package org.haifan.merlin.model.gemini.semantic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/semantic-retrieval/chunks#deletechunkrequest">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteChunkRequest {
    private String name;
}
