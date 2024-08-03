package org.merlin.model.openai.assistants.messages;

import lombok.*;
import org.merlin.model.openai.Attachment;
import org.merlin.model.openai.Content;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    private String role;
    private Content content;
    private List<Attachment> attachments;
    private Map<String, Object> metadata;
}
