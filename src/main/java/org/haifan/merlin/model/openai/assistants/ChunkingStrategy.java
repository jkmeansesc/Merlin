package org.haifan.merlin.model.openai.assistants;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NonNull;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AutoChunkingStrategy.class, name = "auto"),
        @JsonSubTypes.Type(value = StaticChunkingStrategy.class, name = "static"),
        @JsonSubTypes.Type(value = OtherChunkingStrategy.class, name = "other")
})
@Data
public abstract class ChunkingStrategy {
    @NonNull
    private String type;
}
