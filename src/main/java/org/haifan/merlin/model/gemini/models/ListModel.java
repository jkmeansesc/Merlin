package org.haifan.merlin.model.gemini.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <a href="https://ai.google.dev/api/models#response-body_1">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListModel {
    private List<Model> models;
    private String nextPageToken;
}
