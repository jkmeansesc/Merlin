package org.merlin.model.openai.endpoints.finetune;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://platform.openai.com/docs/api-reference/fine-tuning/completions-input">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletionTrainingFormat {
    private String prompt;
    private String completion;
}
