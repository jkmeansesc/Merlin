package org.merlin.model.openai.assistants.vectorstores;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.merlin.model.openai.ChunkingStrategy;
import org.merlin.model.openai.LastError;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VectorStoreFile {
    private String id;
    private String object;
    @JsonProperty("usage_bytes")
    private Long usageBytes;
    @JsonProperty("created_at")
    private Long createdAt;
    @JsonProperty("vector_store_id")
    private String vectorStoreId;
    private String status;
    @JsonProperty("last_error")
    private LastError lastError;
    @JsonProperty("chunking_strategy")
    private Class<ChunkingStrategy> chunkingStrategy;
}
