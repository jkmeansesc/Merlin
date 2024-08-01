package org.haifan.merlin.model.gemini.caching;

import lombok.*;

/**
 * A datatype containing media that is part of a multi-part Content message.
 * <p>
 * A Part consists of data which has an associated datatype. A Part can only contain one of the accepted types in Part.data.
 * <p>
 * A Part must have a fixed IANA MIME type identifying the type and subtype of the media if the inlineData field is filled with raw bytes.
 * <p>
 * <a href="https://ai.google.dev/api/caching#part">...</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Part {
    private String text;
    private Blob inlineData;
    private FunctionCall functionCall;
    private FunctionResponse functionResponse;
    private FileData fileData;
    private ExecutableCode executableCode;
    private CodeExecutionResult codeExecutionResult;
}
