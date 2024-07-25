package org.haifan.merlin.internal.utils;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.haifan.merlin.internal.constants.IanaMediaType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * TODO: add javadoc
 */
public class FileParser {

    private FileParser() {
        throw new IllegalStateException("Utility class");
    }

    public static RequestBody parseFile(File file) {
        try {
            // Detect the MIME type of the file
            String mimeType = Files.probeContentType(file.toPath());

            // If MIME type couldn't be determined, use a fallback
            if (mimeType == null) {
                mimeType = IanaMediaType.OCTET_STREAM;
            }

            // Create the MediaType object
            MediaType mediaType = MediaType.parse(mimeType);

            // Create and return the RequestBody
            return RequestBody.create(file, mediaType);
        } catch (IOException e) {
            throw new FileParserException("Failed to create RequestBody from file: " + file.getName(), e);
        }
    }
}
