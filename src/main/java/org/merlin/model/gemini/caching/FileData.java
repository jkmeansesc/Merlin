package org.merlin.model.gemini.caching;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/caching#filedata">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileData {
    private String mimeType;
    private String fileUri;
}
