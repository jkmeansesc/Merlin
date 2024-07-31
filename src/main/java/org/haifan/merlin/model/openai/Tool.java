package org.haifan.merlin.model.openai;

import lombok.Data;
import lombok.NonNull;

@Data
public class Tool {
    @NonNull
    private String type;

    @NonNull
    private Function function;
}
