package org.haifan.merlin.model.gemini;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ModelList {
    private List<Model> models;
    private String nextPageToken;
}
