package org.haifan.merlin.model.gemini.caching;

import lombok.*;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/rest/v1/Content">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Content {
    private List<Part> parts;
    private String role;
}
