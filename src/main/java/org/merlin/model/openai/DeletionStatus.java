package org.merlin.model.openai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the status of a deletion operation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
