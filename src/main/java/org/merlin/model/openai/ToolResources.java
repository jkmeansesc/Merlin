package org.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolResources {
    @JsonProperty("code_interpreter")
    private CodeInterpreter codeInterpreter;

    @JsonProperty("file_search")
    private FileSearch fileSearch;

    @Data
    public static class CodeInterpreter {
        @JsonProperty("file_ids")
        List<String> fileIds;
    }

    @Data
    public static class FileSearch {
        @JsonProperty("vector_store_ids")
        List<String> vectorStoreIds;
        @JsonProperty("vector_stores")
        List<VectorStoreHelper> vectorStores;

        @Data
        public static class VectorStoreHelper{
            @JsonProperty("file_ids")
            private List<String> fileIds;
            @JsonProperty("chunking_strategy")
            private ChunkingStrategy chunkingStrategy;
            private Map<String, Object> metadata;
        }
    }
}
