package org.haifan.merlin.model.openai.endpoints.finetune;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.haifan.merlin.model.openai.OpenAiMessage;
import org.haifan.merlin.model.openai.Tool;

import java.util.List;

/**
 * <a href="https://platform.openai.com/docs/api-reference/fine-tuning/chat-input">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatTrainingFormat {
    private List<OpenAiMessage> messages;
    private List<Tool> tools;
    @JsonProperty("parallel_tool_calls")
    private Boolean parallelToolCalls;
}
