package org.merlin.model.openai.assistants.assistants;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.merlin.model.openai.AssistantTool;
import org.merlin.model.openai.ResponseFormat;
import org.merlin.model.openai.ToolResources;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
