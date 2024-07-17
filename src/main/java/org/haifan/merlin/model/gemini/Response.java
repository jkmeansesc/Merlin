package org.haifan.merlin.model.gemini;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "@type"
)
@JsonSubTypes({
})
public abstract class Response {
}
