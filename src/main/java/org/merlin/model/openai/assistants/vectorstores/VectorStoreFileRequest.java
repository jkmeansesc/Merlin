package org.merlin.model.openai.assistants.vectorstores;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.merlin.model.openai.ChunkingStrategy;

/**
 * The request object to create vector store files and batches.
 * <a href="https://platform.openai.com/docs/api-reference/vector-stores-files">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VectorStoreFileRequest {
    @NonNull
    @JsonProperty("file_id")
    private String fileId;
    @JsonProperty("chunking_strategy")
    private Class<? extends ChunkingStrategy> chunkingStrategy;
}
