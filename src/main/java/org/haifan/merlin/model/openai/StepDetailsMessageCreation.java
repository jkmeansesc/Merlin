package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.haifan.merlin.internal.constants.Fields;

@EqualsAndHashCode(callSuper = true)
@Data
public class StepDetailsMessageCreation extends StepDetails {
    @JsonProperty("message_creation")
    private MessageCreation messageCreation;

    public StepDetailsMessageCreation() {
        super(Fields.MESSAGE_CREATION);
    }
}
