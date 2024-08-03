package org.merlin.model.openai.endpoints.moderations;

import lombok.*;

/**
 * <a href="https://platform.openai.com/docs/api-reference/moderations/create">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModerationRequest {
    @NonNull
    private String input;
    private String model;
}
