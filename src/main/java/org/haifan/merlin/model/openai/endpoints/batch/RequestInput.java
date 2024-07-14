package org.haifan.merlin.model.openai.endpoints.batch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * The per-line object of the batch input file
 * <a href="https://platform.openai.com/docs/api-reference/batch/request-input">...</a>
 * NOTE: OpenAI documentation seems off, the body field in the example not provided in specification.
 */
@Data
public class RequestInput {

    @JsonProperty("custom_id")
    private String customId;
    private String method;
    private String url;
    private Map<String, Object> body;
}
