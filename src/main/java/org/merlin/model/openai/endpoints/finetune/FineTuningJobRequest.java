package org.merlin.model.openai.endpoints.finetune;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * <a href="https://platform.openai.com/docs/api-reference/fine-tuning/create">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
