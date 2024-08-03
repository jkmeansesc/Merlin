package org.merlin.model.gemini.semantic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/semantic-retrieval/chunks#request-body_7">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteChunkRequests {
    private List<DeleteChunkRequest> requests;
}
