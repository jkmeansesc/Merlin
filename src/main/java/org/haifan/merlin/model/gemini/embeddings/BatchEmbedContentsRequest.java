package org.haifan.merlin.model.gemini.embeddings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Generates multiple embeddings from the model given input text in a synchronous call.
 * <a href="https://ai.google.dev/api/rest/v1/models/batchEmbedContents#EmbedContentRequest">...</a>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BatchEmbedContentsRequest {
    private List<EmbedContentRequest> requests;
}
