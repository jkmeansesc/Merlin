package org.haifan.merlin.model.openai;

import lombok.Data;

/**
 * TODO: add javadoc
 */
@Data
public class DeletionStatus {
    String id;
    String object;
    boolean deleted;
}
