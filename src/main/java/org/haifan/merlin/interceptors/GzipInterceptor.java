package org.haifan.merlin.interceptors;

import okhttp3.*;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;
import org.haifan.merlin.constants.Fields;

import java.io.IOException;

public class GzipInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (isGzipped(originalResponse)) {
            return unzipResponse(originalResponse);
        }
        return originalResponse;
    }

    private boolean isGzipped(Response response) {
        String contentEncoding = response.header(Fields.CONTENT_ENCODING);
        return contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip");
    }

    private Response unzipResponse(Response response) throws IOException {
        if (response.body() == null) {
            return response;
        }

        BufferedSource gzippedSource = response.body().source();
        GzipSource gzipSource = new GzipSource(gzippedSource);
        BufferedSource unzippedSource = Okio.buffer(gzipSource);

        MediaType contentType = response.body().contentType();
        String unzippedBody = unzippedSource.readUtf8();
        ResponseBody unzippedResponseBody = ResponseBody.create(unzippedBody, contentType);

        return response.newBuilder()
                .removeHeader("Content-Encoding")
                .removeHeader("Content-Length")
                .body(unzippedResponseBody)
                .build();
    }
}