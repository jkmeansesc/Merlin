package org.merlin.model.gemini;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * A generic model class represent a list of gemini objects typically return by gemini's list endpoints.
 * The original field name is set to fieldName when incoming Json deserializes.
 *
 * @param <T> the actual data model
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeminiData<T> {
    private List<T> data;
    private String nextPageToken;
    private String fieldName;

    @JsonAnySetter
    public void setData(String fieldName, List<T> data) {
        this.data = data;
        this.fieldName = fieldName;
    }
}
