package org.haifan.merlin.model.openai.endpoints.moderations;

import lombok.Data;

import java.util.List;

/**
 * The moderation object that contains a list of {@link Moderation} results.
 * <a href="https://platform.openai.com/docs/api-reference/moderations/object">...</a>
 */
@Data
public class ModerationList {
    private String id;
    private String model;
    private List<Moderation> results;
}
