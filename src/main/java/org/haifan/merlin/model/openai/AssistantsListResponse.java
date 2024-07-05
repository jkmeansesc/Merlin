package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO: add javadoc
 *
 * @param <T>
 */
@Data
public class AssistantsListResponse<T> {
    private List<T> data;
    private String object;
    @JsonProperty("first_id")
    private String firstId;
    @JsonProperty("last_id")
    private String lastId;
    @JsonProperty("has_more")
    private boolean hasMore;
}
