package org.merlin.model.gemini.files;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * <a href="https://ai.google.dev/api/files#status">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Status {
    private Integer code;
    private String message;
    private List<Map<String, Object>> details;
}
