package org.merlin.model.openai.assistants.threads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.merlin.model.openai.ToolResources;

import java.util.Map;

/**
 * <a href="https://platform.openai.com/docs/api-reference/threads/object">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiThread {
    private String id;
    private String object;
    @JsonProperty("created_at")
    private Long createdAt;
    @JsonProperty("tool_resources")
    private ToolResources toolResources;
    private Map<String, Object> metadata;
}
