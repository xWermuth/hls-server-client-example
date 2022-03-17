package com.hls.server.constants;

import org.springframework.http.MediaType;

public class HeaderConstants {
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String BYTES = "bytes";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final MediaType VIDEO_CONTENT = MediaType.parseMediaType("application/vnd.apple.mpegurl");
}
