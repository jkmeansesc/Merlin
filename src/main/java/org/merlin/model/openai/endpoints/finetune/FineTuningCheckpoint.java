package org.merlin.model.openai.endpoints.finetune;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://platform.openai.com/docs/api-reference/fine-tuning/checkpoint-object">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FineTuningCheckpoint {
    private String id;
    @JsonProperty("created_at")
    private Long createdAt;
    @JsonProperty("fine_tuned_model_checkpoint")
    private String fineTunedModelCheckpoint;
    @JsonProperty("step_number")
    private Integer stepNumber;
    private Metrics metrics;
    @JsonProperty("fine_tuning_job_id")
    private String fineTuningJobId;
    private String object;

    @Data
    public static class Metrics {
        private Integer step;
        @JsonProperty("train_loss")
        private Double trainLoss;
        @JsonProperty("train_mean_token_accuracy")
        private Double trainMeanTokenAccuracy;
        @JsonProperty("valid_loss")
        private Double validLoss;
        @JsonProperty("valid_mean_token_accuracy")
        private Double validMeanTokenAccuracy;
        @JsonProperty("full_valid_loss")
        private Double fullValidLoss;
        @JsonProperty("full_valid_mean_token_accuracy")
        private Double fullValidMeanTokenAccuracy;
    }
}
