package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MessageCreation {
    @JsonProperty("message_id")
    private String messageId;
}
