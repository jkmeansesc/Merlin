package org.haifan.merlin.model.gemini.semantic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/semantic-retrieval/chunks#request-body_7">...</a>
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteChunkRequests {
    private List<DeleteChunkRequest> requests;
}
