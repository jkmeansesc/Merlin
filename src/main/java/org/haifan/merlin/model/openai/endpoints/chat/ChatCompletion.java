package org.haifan.merlin.model.openai.endpoints.chat;

import lombok.*;


/**
 * <a href="https://platform.openai.com/docs/api-reference/chat/object">...</a>
 */
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChatCompletion extends BaseChatCompletion {
    // no additional fields
}
