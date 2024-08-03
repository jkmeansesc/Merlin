package org.merlin.model.openai.assistants.vectorstores;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.merlin.model.openai.ChunkingStrategy;
import org.merlin.model.openai.ExpiresAfter;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VectorStoreRequest {
    @JsonProperty("file_ids")
    private List<String> fileIds;
    private String name;
    @JsonProperty("expires_after")
    private ExpiresAfter expiresAfter;
    @JsonProperty("chunking_strategy")
    private Class<? extends ChunkingStrategy> chunkingStrategy;
    private Map<String, Object> metadata;
}
