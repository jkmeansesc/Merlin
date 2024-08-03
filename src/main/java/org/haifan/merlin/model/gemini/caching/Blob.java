package org.haifan.merlin.model.gemini.caching;

import lombok.*;

/**
 * <a href="https://ai.google.dev/api/caching#blob">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Blob {
    private String mimeType;
    private String data;
}
