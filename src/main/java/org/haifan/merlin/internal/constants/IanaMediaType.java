package org.haifan.merlin.internal.constants;

/**
 * <a href="https://www.iana.org/assignments/media-types/media-types.xhtml">...</a>
 */
public class IanaMediaType {

    private IanaMediaType() {
        throw new IllegalStateException("Utility class");
    }

    public static final String IMAGE_ALL = "image/*";
    public static final String OCTET_STREAM = "application/octet-stream";
    public static final String JSON = "application/json";
    public static final String FORM_DATA = "multipart/form-data";
    public static final String PROBLEM_JSON = "application/problem+json";
}
