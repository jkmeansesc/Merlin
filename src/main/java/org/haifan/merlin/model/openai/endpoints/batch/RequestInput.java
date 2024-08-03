package org.haifan.merlin.model.openai.endpoints.batch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * <a href="https://platform.openai.com/docs/api-reference/batch/request-input">...</a>
 * NOTE: OpenAI documentation seems off, the body field in the example not provided in specification.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestInput {

    @JsonProperty("custom_id")
    private String customId;
    private String method;
    private String url;
    private Map<String, Object> body;
}
