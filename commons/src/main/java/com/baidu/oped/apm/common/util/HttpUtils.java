
package com.baidu.oped.apm.common.util;

/**
 * class HttpUtils 
 *
 * @author meidongxu@baidu.com
 */
public final class HttpUtils {

    private static final String UTF8 = "UTF-8";

    private static final String CHARSET = "charset=";

    private HttpUtils() {
    }

    public static String parseContentTypeCharset(String contentType) {
        return parseContentTypeCharset(contentType, UTF8);
    }

    public static String parseContentTypeCharset(String contentType, String defaultCharset) {
        if (contentType == null) {
            // default spec specifies iso-8859-1, but most WASes set it to UTF-8.
            // might be better to make it configurable
            return defaultCharset;
        }
        int charsetStart = contentType.indexOf(CHARSET);
        if (charsetStart == -1) {
            // none
            return defaultCharset;
        }
        charsetStart = charsetStart + CHARSET.length();
        int charsetEnd = contentType.indexOf(';', charsetStart);
        if (charsetEnd == -1) {
            charsetEnd = contentType.length();
        }
        contentType = contentType.substring(charsetStart, charsetEnd);

        return contentType.trim();
    }
}
