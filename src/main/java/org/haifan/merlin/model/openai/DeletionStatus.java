package org.haifan.merlin.model.openai;

import lombok.Data;

/**
 * Represents the status of a deletion operation.
 */
@Data
public class DeletionStatus {
    /**
     * The unique identifier of the deleted object.
     */
    private String id;
    /**
     * The type of the object that was deleted.
     */
    private String object;
    /**
     * A flag indicating whether the deletion was successful.
     */
    private boolean deleted;
}
