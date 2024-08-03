package org.haifan.merlin.model.openai.assistants.threads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.haifan.merlin.model.openai.ToolResources;
import org.haifan.merlin.model.openai.assistants.messages.MessageRequest;

import java.util.List;
import java.util.Map;

/**
 * <a href="https://platform.openai.com/docs/api-reference/threads/createThread">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThreadRequest {
    private List<MessageRequest> messages;
    @JsonProperty("tool_resources")
    private ToolResources toolResources;
    private Map<String, Object> metadata;
}
