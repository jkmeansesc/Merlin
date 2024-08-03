package org.haifan.merlin.model.openai.endpoints.finetune;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://platform.openai.com/docs/api-reference/fine-tuning/object#fine-tuning/object-hyperparameters">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hyperparameters {
    @JsonProperty("batch_size")
    private String batchSize;

    @JsonProperty("learning_rate_multiplier")
    private String learningRateMultiplier;

    @JsonProperty("n_epochs")
    private String nEpochs;
}
