package org.merlin.model.openai.assistants.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.merlin.model.openai.Attachment;
import org.merlin.model.openai.ContentPart;
import org.merlin.model.openai.IncompleteDetails;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    // Not documented by openai but present in its example
    // see https://platform.openai.com/docs/api-reference/messages/modifyMessage
    @JsonProperty("file_ids")
    private List<String> fileIds;
}
