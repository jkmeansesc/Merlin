package org.merlin.model.gemini.caching;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * <a href="https://ai.google.dev/api/caching#schema">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schema {
    private Type type;
    private String format;
    private String description;
    private Boolean nullable;
    @JsonProperty("enum")
    private List<String> enums;
    private Map<String, Object> properties;
    private List<String> required;
    private Schema items;
}
