package org.haifan.merlin.model.openai;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.haifan.merlin.internal.constants.Fields;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class CodeInterpreterTool extends AssistantTool {

    public CodeInterpreterTool() {
        super(Fields.CODE_INTERPRETER);
    }
}
