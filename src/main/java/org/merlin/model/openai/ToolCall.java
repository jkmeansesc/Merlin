package org.merlin.model.openai;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolCall {
    private Integer index;

    @NonNull
    private String id;

    @NonNull
    private String type;

    @NonNull
    private Function function;

    @Data
    public static class Function {

        @NonNull
        private String name;

        @NonNull
        private String arguments;
    }
}
