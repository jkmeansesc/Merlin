package org.haifan.merlin.model.openai.assistants.vectorstores;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.haifan.merlin.model.openai.assistants.ChunkingStrategy;

/**
 * The request object to create vector store files and batches.
 * <a href="https://platform.openai.com/docs/api-reference/vector-stores-files">...</a>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VectorStoreFileRequest {
    @NonNull
    @JsonProperty("file_id")
    private String fileId;
    @JsonProperty("chunking_strategy")
    private Class<? extends ChunkingStrategy> chunkingStrategy;
}
