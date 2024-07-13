package org.haifan.merlin.model.openai.endpoints.finetune;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * The request model object for creating a fine-tuning job for OpenAI.
 * <a href="https://platform.openai.com/docs/api-reference/fine-tuning/create">...</a>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FineTuningJobRequest {

    @NonNull
    private String model;

    @NonNull
    @JsonProperty("training_file")
    private String trainingFile;

    private Hyperparameters hyperparameters;

    private String suffix;

    @JsonProperty("validation_file")
    private String validationFile;

    private List<Integration> integrations;

    private Integer seed;
}
