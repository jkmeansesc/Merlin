package org.haifan.merlin.model.openai.endpoints.finetune;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class Integration {
    @NonNull
    private String type;

    @NonNull
    private Wandb wandb;

    @Data
    public static class Wandb {
        @NonNull
        private String project;
        private String name;
        private String entity;
        private List<String> tags;
    }
}
