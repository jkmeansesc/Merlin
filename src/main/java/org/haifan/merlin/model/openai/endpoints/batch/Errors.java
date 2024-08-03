package org.haifan.merlin.model.openai.endpoints.batch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <a href="https://platform.openai.com/docs/api-reference/batch/object#batch/object-errors">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Errors {
    private String object;
    private List<Error> data;
}
