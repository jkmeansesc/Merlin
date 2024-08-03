package org.haifan.merlin.model.openai.assistants.streaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.haifan.merlin.model.openai.ContentPart;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDelta {
    private String id;
    private String object;
    private Delta delta;

    @Data
    public static class Delta {
        private String role;
        private List<ContentPart> content;
    }
}
