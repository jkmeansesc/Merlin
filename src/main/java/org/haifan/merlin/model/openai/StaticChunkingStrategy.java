package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.haifan.merlin.constants.Fields;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class StaticChunkingStrategy extends ChunkingStrategy {

    @NonNull
    @JsonProperty("static")
    private Static staticStrategy;

    public StaticChunkingStrategy(@NotNull Static staticStrategy) {
        super(Fields.STATIC);
        this.staticStrategy = staticStrategy;
    }

    @Data
    public static class Static {
        @NonNull
        @JsonProperty("max_chunk_size_tokens")
        private Integer maxChunkSizeTokens;
        @NonNull
        @JsonProperty("chunk_overlap_tokens")
        private Integer chunkOverlapTokens;
    }
}