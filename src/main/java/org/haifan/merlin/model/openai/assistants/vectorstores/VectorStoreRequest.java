package org.haifan.merlin.model.openai.assistants.vectorstores;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.haifan.merlin.model.openai.assistants.ChunkingStrategy;
import org.haifan.merlin.model.openai.assistants.ExpiresAfter;

import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
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
