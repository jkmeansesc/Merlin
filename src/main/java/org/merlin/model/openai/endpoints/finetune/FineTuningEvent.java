package org.merlin.model.openai.endpoints.finetune;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * <a href="https://platform.openai.com/docs/api-reference/fine-tuning/event-object">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
