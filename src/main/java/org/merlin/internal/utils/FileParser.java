package org.merlin.internal.utils;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.merlin.internal.constants.IanaMediaType;
import org.jetbrains.annotations.NotNull;

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
        String mimeType = getMimeType(file);
        MediaType mediaType = MediaType.parse(mimeType);
        return RequestBody.create(file, mediaType);
    }

    /**
     * Returns the MIME type of the specified file.
     * <p>
     * This method attempts to determine the MIME type of the provided file by probing
     * its content using the {@link Files#probeContentType(java.nio.file.Path)} method.
     * If the MIME type cannot be determined, it defaults to {@code application/octet-stream}.
     * </p>
     *
     * @param file the file for which to determine the MIME type
     * @return the MIME type of the file, or {@code application/octet-stream} if the MIME type cannot be determined
     * @throws NullPointerException if the specified file is {@code null}
     */
    public static @NotNull String getMimeType(File file) {
        String mimeType;
        try {
            mimeType = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            throw new FileParserException("Failed to retrieve mimeType from file: " + file.getName(), e);
        }

        if (mimeType == null) {
            mimeType = IanaMediaType.OCTET_STREAM;
        }
        return mimeType;
    }
}
