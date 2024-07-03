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
public class OpenAiResponse<T> {
    /**
     * A list containing the actual results
     */
    private List<T> data;

    /**
     * The type of object returned
     */
    private String object;

    /**
     * The first id included
     */
    @JsonProperty("first_id")
    private String firstId;

    /**
     * The last id included
     */
    @JsonProperty("last_id")
    private String lastId;

    /**
     * True if there are objects after lastId
     */
    @JsonProperty("has_more")
    private boolean hasMore;
}
