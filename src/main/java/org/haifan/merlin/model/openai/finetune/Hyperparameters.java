package org.haifan.merlin.model.openai.finetune;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Hyperparameters {
    @JsonProperty("batch_size")
    private String batchSize;

    @JsonProperty("learning_rate_multiplier")
    private String learningRateMultiplier;

    @JsonProperty("n_epochs")
    private String nEpochs;
}
