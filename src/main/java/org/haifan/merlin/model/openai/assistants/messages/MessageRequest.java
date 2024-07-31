package org.haifan.merlin.model.openai.assistants.messages;

import lombok.*;
import org.haifan.merlin.model.openai.Attachment;
import org.haifan.merlin.model.openai.Content;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    @NonNull
    private String role;
    private Content content;
    private List<Attachment> attachments;
    private Map<String, Object> metadata;
}
