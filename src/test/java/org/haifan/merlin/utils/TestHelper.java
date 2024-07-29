package org.haifan.merlin.utils;

import okhttp3.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TestHelper {

    /**
     * Read file content into a String.
     */
    public static String read(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get("src/test/resources/" + filePath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calculate the SHA-256 checksum of a file.
     */
    public static String getSHA256(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192]; // Read in 8KB chunks
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }
        byte[] hash = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            // Convert each byte to a 2-digit hexadecimal value
            String hex = Integer.toHexString(0xff & b);
            // Pad with leading zero if necessary
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Gets a File object from a path in the test resources folder.
     */
    public static File getFile(String path) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resourceUrl = classLoader.getResource(path);
        if (resourceUrl == null) {
            throw new IOException("Resource not found: " + path);
        }
        return new File(resourceUrl.getFile());
    }

    public static Path audioToFile(ResponseBody body, Path path) throws IOException {
        if (body == null) {
            throw new IOException("Null response body");
        }
        try (InputStream inputStream = body.byteStream()) {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        }
        return path;
    }

    /**
     * Writes the content of a ResponseBody to a file in the test resources directory.
     */
    public static Path writeResponseBodyToFile(ResponseBody responseBody, Path filePath) throws IOException {
        Path resourceDirectory = Path.of("src", "test", "resources");
        Path fullPath = resourceDirectory.resolve(filePath);
        Files.createDirectories(fullPath.getParent());
        try (InputStream inputStream = responseBody.byteStream()) {
            Files.copy(inputStream, fullPath, StandardCopyOption.REPLACE_EXISTING);
        }
        return fullPath;
    }
}
