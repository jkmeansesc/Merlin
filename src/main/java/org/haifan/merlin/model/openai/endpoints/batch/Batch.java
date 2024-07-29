package org.haifan.merlin.model.openai.endpoints.batch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * The batch object
 * <a href="https://platform.openai.com/docs/api-reference/batch/object">...</a>
 */
@Data
public class Batch {
    private String id;
    private String object;
    private String endpoint;
    private Errors errors;
    @JsonProperty("input_file_id")
    private String inputFileId;
    @JsonProperty("completion_window")
    private String completionWindow;
    private String status;
    @JsonProperty("output_file_id")
    private String outputFileId;
    @JsonProperty("error_file_id")
    private String errorFileId;
    @JsonProperty("created_at")
    private Long createdAt;
    @JsonProperty("in_progress_at")
    private Long inProgressAt;
    @JsonProperty("expires_at")
    private Long expiresAt;
    @JsonProperty("finalizing_at")
    private Long finalizingAt;
    @JsonProperty("completed_at")
    private Long completedAt;
    @JsonProperty("failed_at")
    private Long failedAt;
    @JsonProperty("expired_at")
    private Long expiredAt;
    @JsonProperty("cancelling_at")
    private Long cancellingAt;
    @JsonProperty("cancelled_at")
    private Long cancelledAt;
    @JsonProperty("request_counts")
    private RequestCounts requestCounts;
    private Map<String, Object> metadata;

    @Data
    public static class RequestCounts {
        private Integer total;
        private Integer completed;
        private Integer failed;
    }

    @Data
    public static class Errors {
        private String object;
        private List<Error> data;
    }
}
