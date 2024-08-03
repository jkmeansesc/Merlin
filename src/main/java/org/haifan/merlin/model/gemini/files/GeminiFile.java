package org.haifan.merlin.model.gemini.files;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.haifan.merlin.model.gemini.State;

/**
 * <a href="https://ai.google.dev/api/files#resource:-file">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeminiFile {
    private String name;
    private String displayName;
    private String mimeType;
    private String sizeBytes;
    private String createTime;
    private String updateTime;
    private String expirationTime;
    private String sha256Hash;
    private String uri;
    private State state;
    private Status error;
}
