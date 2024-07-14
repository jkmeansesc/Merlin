package org.haifan.merlin.model.openai.assistants.threads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.haifan.merlin.model.openai.ToolResources;

import java.util.Map;

/**
 * Represents a thread that contains messages.
 * <a href="https://platform.openai.com/docs/api-reference/threads/object">...</a>
 */
@Data
public class ThreadObject {
    private String id;
    private String object;
    @JsonProperty("created_at")
    private Long createdAt;
    @JsonProperty("tool_resources")
    private ToolResources toolResources;
    private Map<String, Object> metadata;
}
