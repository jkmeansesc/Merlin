package org.merlin.model.openai;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.merlin.internal.constants.Fields;

@Data
@EqualsAndHashCode(callSuper = true)
public class OtherChunkingStrategy extends ChunkingStrategy {

    public OtherChunkingStrategy() {
        super(Fields.OTHER);
    }
}
