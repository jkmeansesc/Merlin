package org.haifan.merlin.model.openai.assistants.assistants;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.haifan.merlin.model.openai.ResponseFormat;
import org.haifan.merlin.model.openai.assistants.AssistantTool;
import org.haifan.merlin.model.openai.assistants.ToolResources;

import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssistantRequest {
    @NonNull
    private String model;
    private String name;
    private String description;
    private String instructions;
    private List<AssistantTool> tools;
    @JsonProperty("tool_resources")
    private ToolResources toolResources;
    private Map<String, Object> metadata;
    private Double temperature;
    @JsonProperty("top_p")
    private Double topP;
    @JsonProperty("response_format")
    private ResponseFormat responseFormat;
}
