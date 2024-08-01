package org.haifan.merlin.model.gemini.files;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://ai.google.dev/api/files#videometadata">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoMetadata {
    private String videoDuration;
}
