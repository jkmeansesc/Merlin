package org.haifan.merlin.model.openai;

import lombok.Data;

/**
 * TODO: add javadoc
 */
@Data
public class DeletionStatus {
    private String id;
    private String object;
    private boolean deleted;
}
