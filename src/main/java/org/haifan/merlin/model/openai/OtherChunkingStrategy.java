package org.haifan.merlin.model.openai;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.haifan.merlin.internal.constants.Fields;

@Data
@EqualsAndHashCode(callSuper = true)
public class OtherChunkingStrategy extends ChunkingStrategy {

    public OtherChunkingStrategy() {
        super(Fields.OTHER);
    }
}
