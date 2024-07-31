package org.haifan.merlin.model.openai.assistants.vectorstores;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.haifan.merlin.model.openai.ChunkingStrategy;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VectorStoreFileBatchRequest {
    @JsonProperty("file_ids")
    private List<String> fileIds;
    @JsonProperty("chunking_strategy")
    private Class<? extends ChunkingStrategy> chunkingStrategy;
}
