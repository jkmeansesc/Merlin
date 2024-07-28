package org.haifan.merlin.model.gemini;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * This resource represents a long-running operation that is the result of a network API call.
 * <a href="https://ai.google.dev/api/rest/Shared.Types/Operation#Status">...</a>
 */
@Data
public class Operation {
    private String name;
    private Boolean done;
    private Metadata metadata;
    private Status error;
    private Response response;

    @JsonIgnore
    public Object getResult() {
        return Boolean.TRUE.equals(done) ? getResultWhenDone() : null;
    }

    @JsonIgnore
    private Object getResultWhenDone() {
        return error != null ? error : response;
    }

    @JsonIgnore
    public void setResult(Object result) {
        if (result instanceof Status status) {
            this.error = status;
            this.response = null;
        } else {
            this.error = null;
            this.response = (Response) result;
        }
        this.done = true;
    }
}
