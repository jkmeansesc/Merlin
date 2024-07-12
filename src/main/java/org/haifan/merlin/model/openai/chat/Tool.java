package org.haifan.merlin.model.openai.chat;

import lombok.*;

@Data
public class Tool {
    @NonNull
    private String type;

    @NonNull
    private Function function;

    @Data
    public static class Function {
        private String description;

        @NonNull
        private String name;

        private Object parameters;
    }
}
