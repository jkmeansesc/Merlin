package org.merlin.model.gemini.files;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/files#response-body">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadMediaResponse {
    private GeminiFile file;
}
