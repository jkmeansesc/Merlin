package org.haifan.merlin.internal.utils;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.haifan.merlin.internal.constants.IanaMediaType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * A utility class for parsing files into {@link RequestBody} objects.
 * This class provides a method to convert a file into a {@link RequestBody} suitable
 * for HTTP requests, with appropriate MIME type detection.
 */
public class FileParser {

    /**
     * Private constructor to prevent instantiation.
     * Throws an {@link IllegalStateException} if called.
     */
    private FileParser() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Parses a given file into a {@link RequestBody} object.
     * The method detects the MIME type of the file and uses it to create the {@link RequestBody}.
     * If the MIME type cannot be determined, a default MIME type of "application/octet-stream" is used.
     *
     * @param file the file to be parsed into a {@link RequestBody}.
     * @return a {@link RequestBody} containing the file's content and MIME type.
     * @throws FileParserException if an I/O error occurs while reading the file.
     */
    public static RequestBody parseFile(File file) {
        try {
            String mimeType = Files.probeContentType(file.toPath());
            if (mimeType == null) {
                mimeType = IanaMediaType.OCTET_STREAM;
            }
            MediaType mediaType = MediaType.parse(mimeType);
            return RequestBody.create(file, mediaType);
        } catch (IOException e) {
            throw new FileParserException("Failed to create RequestBody from file: " + file.getName(), e);
        }
    }
}
