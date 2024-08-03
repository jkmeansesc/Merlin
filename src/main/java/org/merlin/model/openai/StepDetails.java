package org.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StepDetailsMessageCreation.class, name = "message_creation"),
        @JsonSubTypes.Type(value = StepDetailsToolCalls.class, name = "tool_calls"),
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class StepDetails {
    private String type;
}
