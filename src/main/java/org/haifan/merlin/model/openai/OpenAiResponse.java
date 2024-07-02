package org.haifan.merlin.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO: add javadoc
 * @param <T>
 */
@Data
public class OpenAiResponse<T> {
    /**
     * A list containing the actual results
     */
    public List<T> data;

    /**
     * The type of object returned
     */
    public String object;

    /**
     * The first id included
     */
    @JsonProperty("first_id")
    public String firstId;

    /**
     * The last id included
     */
    @JsonProperty("last_id")
    public String lastId;

    /**
     * True if there are objects after lastId
     */
    @JsonProperty("has_more")
    public boolean hasMore;
}
