package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

@Data
public class TruncationStrategy {
    @NonNull
    private String type;
    @JsonProperty("last_messages")
    private Integer lastMessages;
}
