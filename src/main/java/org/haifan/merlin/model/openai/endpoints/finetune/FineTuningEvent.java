package org.haifan.merlin.model.openai.endpoints.finetune;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * Fine-tuning job event object.
 * <a href="https://platform.openai.com/docs/api-reference/fine-tuning/event-object">...</a>
 */
@Data
public class FineTuningEvent {
    private String id;
    @JsonProperty("created_at")
    private Long createdAt;
    private String level;
    private String message;
    private String object;
    private Map<String, Object> data;
    private String type;
}
