package org.haifan.merlin.model.openai.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.haifan.merlin.utils.ToolChoiceDeserializer;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = ToolChoiceDeserializer.class)
public class ToolChoice {
    private Object value;

    @JsonIgnore
    public ToolChoice addToolChoice(String toolChoice) {
        this.value = toolChoice;
        return this;
    }

    @JsonIgnore
    public ToolChoice addToolChoice(Tool toolChoice) {
        this.value = toolChoice;
        return this;
    }
}
