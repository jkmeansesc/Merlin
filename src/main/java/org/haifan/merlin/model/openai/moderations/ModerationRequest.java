package org.haifan.merlin.model.openai.moderations;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * The request body model for creating a moderation request.
 * <a href="https://platform.openai.com/docs/api-reference/moderations/create">...</a>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModerationRequest {

    @NonNull
    private String input;

    private String model;
}
