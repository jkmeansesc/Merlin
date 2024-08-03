package org.haifan.merlin.model.openai.endpoints.moderations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The moderation object that contains a list of {@link Moderation} results.
 * <a href="https://platform.openai.com/docs/api-reference/moderations/object">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModerationList {
    private String id;
    private String model;
    private List<Moderation> results;
}
