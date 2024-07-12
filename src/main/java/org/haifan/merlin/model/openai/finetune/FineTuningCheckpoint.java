package org.haifan.merlin.model.openai.finetune;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The fine_tuning.job.checkpoint object represents a model checkpoint for a fine-tuning job that is ready to use.
 * <a href="https://platform.openai.com/docs/api-reference/fine-tuning/checkpoint-object">...</a>
 */
@Data
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
