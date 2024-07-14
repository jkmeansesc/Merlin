package org.haifan.merlin.model.openai;

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
        @JsonSubTypes.Type(value = CodeInterpreterTool.class, name = "code_interpreter"),
        @JsonSubTypes.Type(value = FileSearchTool.class, name = "file_search"),
        @JsonSubTypes.Type(value = FunctionTool.class, name = "function")
})
@Data
public abstract class AssistantTool {
    @NonNull
    private String type;
}
