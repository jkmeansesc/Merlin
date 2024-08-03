package org.merlin.model.gemini.files;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/files#request-body">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadMediaRequest {
    private GeminiFile file;
}
