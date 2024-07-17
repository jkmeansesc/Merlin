package org.haifan.merlin.model.gemini;

import lombok.Data;

import java.util.List;

/**
 * The response message for Operations.ListOperations.
 * <a href="https://ai.google.dev/api/rest/v1/ListOperationsResponse">...</a>
 */
@Data
public class ListOperationsResponse {
    private List<Operation> operations;
    private String nextPageToken;
}
