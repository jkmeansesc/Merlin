package org.haifan.merlin.model.openai.endpoints.batch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * The per-line object of the batch output and error files.
 * <a href="https://platform.openai.com/docs/api-reference/batch/request-output">...</a>
 */
@Data
public class RequestOutput {
    private String id;
    @JsonProperty("custom_id")
    private String customId;
    private OutputResponse response;
    private Error error;

    @Data
    public static class OutputResponse {
        @JsonProperty("status_code")
        private Integer statusCode;
        @JsonProperty("request_id")
        private String requestId;
        private Map<String, Object> body;
    }
}
