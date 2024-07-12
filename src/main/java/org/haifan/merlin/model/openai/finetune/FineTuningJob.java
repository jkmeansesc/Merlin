package org.haifan.merlin.model.openai.finetune;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * The fine_tuning.job object represents a fine-tuning job that has been created through the API.
 * <a href="https://platform.openai.com/docs/api-reference/fine-tuning/object">...</a>
 */
@Data
public class FineTuningJob {
    private String id;
    @JsonProperty("created_at")
    private Long createdAt;
    private Error error;
    @JsonProperty("fine_tuned_model")
    private String fineTunedModel;
    @JsonProperty("finished_at")
    private Long finishedAt;
    private Hyperparameters hyperparameters;
    private String model;
    private String object;
    @JsonProperty("organization_id")
    private String organizationId;
    @JsonProperty("result_files")
    private List<String> resultFiles;
    private String status;
    @JsonProperty("trained_tokens")
    private Integer trainedTokens;
    @JsonProperty("training_file")
    private String trainingFile;
    @JsonProperty("validation_file")
    private String validationFile;
    private List<Integration> integrations;
    private Integer seed;
    @JsonProperty("estimated_finish")
    private Integer estimatedFinish;

    @Data
    public static class Error {
        private String code;
        private String message;
        private String param;
    }
}
