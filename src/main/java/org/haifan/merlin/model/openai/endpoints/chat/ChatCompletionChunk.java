package org.haifan.merlin.model.openai.endpoints.chat;

import lombok.*;

/**
 * <a href="https://platform.openai.com/docs/api-reference/chat/streaming">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChatCompletionChunk extends BaseChatCompletion {
    // no additional fields
}
