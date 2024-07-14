package org.haifan.merlin.model.openai;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.haifan.merlin.constants.Fields;

@Data
@EqualsAndHashCode(callSuper = true)
public class CodeInterpreterTool extends AssistantTool {

    public CodeInterpreterTool() {
        super(Fields.CODE_INTERPRETER);
    }
}
