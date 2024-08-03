package org.merlin.model.openai;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.merlin.internal.constants.Fields;

@EqualsAndHashCode(callSuper = true)
@Data
public class FunctionTool extends AssistantTool {

    @NonNull
    private Function function;

    public FunctionTool(@NonNull Function function) {
        super(Fields.FUNCTION);
        this.function = function;
    }
}
