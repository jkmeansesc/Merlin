package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * A wrapper object typically returned by OpenAI's listing endpoints, such as list models.
 */
@Data
public class OpenAiList<T> {
    private List<T> data;
    private String object;
    @JsonProperty("first_id")
    private String firstId;
    @JsonProperty("last_id")
    private String lastId;
    @JsonProperty("has_more")
    private boolean hasMore;
}
