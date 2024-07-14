package org.haifan.merlin.model.openai.assistants.threads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.haifan.merlin.model.openai.Message;
import org.haifan.merlin.model.openai.ToolResources;

import java.util.List;
import java.util.Map;

/**
 * The request object to create a thread for OpenAI
 * <a href="https://platform.openai.com/docs/api-reference/threads/createThread">...</a>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ThreadRequest {
    private List<Message> messages;
    @JsonProperty("tool_resources")
    private ToolResources toolResources;
    private Map<String, Object> metadata;
}
