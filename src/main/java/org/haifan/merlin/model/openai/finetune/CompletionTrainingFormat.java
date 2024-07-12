package org.haifan.merlin.model.openai.finetune;

import lombok.Data;

/**
 * The per-line training example of a fine-tuning input file for completions models.
 * <a href="https://platform.openai.com/docs/api-reference/fine-tuning/completions-input">...</a>
 */
@Data
public class CompletionTrainingFormat {
    private String prompt;
    private String completion;
}
