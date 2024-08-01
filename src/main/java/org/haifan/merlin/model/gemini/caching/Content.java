package org.haifan.merlin.model.gemini.caching;

import lombok.*;

import java.util.List;

/**
 * The base structured datatype containing multi-part content of a message.
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
