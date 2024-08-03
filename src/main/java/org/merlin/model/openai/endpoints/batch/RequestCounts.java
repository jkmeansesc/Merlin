package org.merlin.model.openai.endpoints.batch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="https://platform.openai.com/docs/api-reference/batch/object#batch/object-request_counts">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestCounts {
    private Integer total;
    private Integer completed;
    private Integer failed;
}
