package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * A wrapper object typically returned by OpenAI's listing endpoints, such as list models.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiData<T> {
    private List<T> data;
    private String object;
    @JsonProperty("first_id")
    private String firstId;
    @JsonProperty("last_id")
    private String lastId;
    @JsonProperty("has_more")
    private Boolean hasMore;
    private String model;
    private Usage usage;
    private Long created;
}
