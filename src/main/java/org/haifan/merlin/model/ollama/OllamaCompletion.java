package org.haifan.merlin.model.ollama;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO: add javadoc
 */
@Data
public class OllamaCompletion {
    private String model;
    @JsonProperty("created_at")
    private String createdAt;
    private String response;
    private OllamaMessage message;
    private Boolean done;
    @JsonProperty("total_duration")
    private Long totalDuration;
    @JsonProperty("load_duration")
    private Long loadDuration;
    @JsonProperty("prompt_eval_count")
    private Integer promptEvalCount;
    @JsonProperty("prompt_eval_duration")
    private Long promptEvalDuration;
    @JsonProperty("eval_count")
    private Integer evalCount;
    @JsonProperty("eval_duration")
    private Long evalDuration;
    private List<Integer> context;
    @JsonProperty("done_reason")
    private String doneReason;

}
