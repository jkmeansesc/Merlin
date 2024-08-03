package org.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TruncationStrategy {
    @NonNull
    private String type;
    @JsonProperty("last_messages")
    private Integer lastMessages;
}
