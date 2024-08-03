package org.merlin.model.openai.endpoints.batch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://platform.openai.com/docs/api-reference/batch/object#batch/object-errors">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Error {
    private String code;
    private String message;
    private String param;
    private Integer line;
}
