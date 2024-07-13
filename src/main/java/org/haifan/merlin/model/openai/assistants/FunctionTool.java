package org.haifan.merlin.model.openai.assistants;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.haifan.merlin.constants.Fields;
import org.haifan.merlin.model.openai.endpoints.Function;
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
