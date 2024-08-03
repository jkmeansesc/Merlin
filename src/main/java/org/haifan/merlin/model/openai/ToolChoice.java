package org.haifan.merlin.model.openai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolChoice {
    private String type;
    private Tool tool;

    public ToolChoice(String type) {
        this.type = type;
    }

    public ToolChoice(Tool tool) {
        this.tool = tool;
    }
}
