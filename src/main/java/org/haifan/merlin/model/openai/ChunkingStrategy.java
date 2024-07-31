package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AutoChunkingStrategy.class, name = "auto"),
        @JsonSubTypes.Type(value = StaticChunkingStrategy.class, name = "static"),
        @JsonSubTypes.Type(value = OtherChunkingStrategy.class, name = "other")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class ChunkingStrategy {
    @NonNull
    private String type;
}
