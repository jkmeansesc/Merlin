package org.haifan.merlin.model.openai.assistants.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.haifan.merlin.model.openai.Attachment;
import org.haifan.merlin.model.openai.ContentPart;
import org.haifan.merlin.model.openai.IncompleteDetails;

import java.util.List;
import java.util.Map;

@Data
public class MessageObject {
    private String id;
    private String object;
    @JsonProperty("created_at")
    private Long createdAt;
    @JsonProperty("thread_id")
    private String threadId;
    private String status;
    @JsonProperty("incomplete_details")
    private IncompleteDetails incompleteDetails;
    @JsonProperty("completed_at")
    private Long completedAt;
    @JsonProperty("incomplete_at")
    private Long incompleteAt;
    private String role;
    private List<ContentPart> content;
    @JsonProperty("assistant_id")
    private String assistantId;
    @JsonProperty("run_id")
    private String runId;
    private List<Attachment> attachments;
    private Map<String, Object> metadata;
}
