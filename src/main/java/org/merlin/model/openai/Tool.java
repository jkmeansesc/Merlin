package org.merlin.model.openai;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tool {
    @NonNull
    private String type;

    @NonNull
    private Function function;
}
