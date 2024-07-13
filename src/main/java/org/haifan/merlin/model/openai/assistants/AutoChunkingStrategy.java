package org.haifan.merlin.model.openai.assistants;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.haifan.merlin.constants.Fields;

@EqualsAndHashCode(callSuper = true)
@Data
public class AutoChunkingStrategy extends ChunkingStrategy {

    public AutoChunkingStrategy() {
        super(Fields.AUTO);
    }
}
