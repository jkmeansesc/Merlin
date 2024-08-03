package org.merlin.model.openai.endpoints.finetune;

import lombok.*;

import java.util.List;

/**
 * <a href="https://platform.openai.com/docs/api-reference/fine-tuning/object#fine-tuning/object-integrations">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
