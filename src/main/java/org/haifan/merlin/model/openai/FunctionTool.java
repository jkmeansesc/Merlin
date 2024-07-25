package org.haifan.merlin.model.openai;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.haifan.merlin.internal.constants.Fields;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class FunctionTool extends AssistantTool {

    @NonNull
    private Function function;

    public FunctionTool(@NotNull Function function) {
        super(Fields.FUNCTION);
        this.function = function;
    }
}
