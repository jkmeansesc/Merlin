package org.haifan.merlin.model.openai.endpoints.finetune;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.haifan.merlin.model.openai.OpenAiMessage;
import org.haifan.merlin.model.openai.Tool;

import java.util.List;

/**
 * The per-line training example of a fine-tuning input file for chat models.
 * <a href="https://platform.openai.com/docs/api-reference/fine-tuning/chat-input">...</a>
 */
@Data
public class ChatTrainingFormat {
    private List<OpenAiMessage> messages;
    private List<Tool> tools;
    @JsonProperty("parallel_tool_calls")
    private Boolean parallelToolCalls;
}
