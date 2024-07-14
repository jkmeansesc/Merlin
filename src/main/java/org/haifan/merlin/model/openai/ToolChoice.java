package org.haifan.merlin.model.openai;

import lombok.Data;

@Data
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
