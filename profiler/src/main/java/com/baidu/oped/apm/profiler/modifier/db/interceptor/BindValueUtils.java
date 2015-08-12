
package com.baidu.oped.apm.profiler.modifier.db.interceptor;

import com.baidu.oped.apm.bootstrap.util.StringUtils;

/**
 * class BindValueUtils 
 *
 * @author meidongxu@baidu.com
 */
public class BindValueUtils {

    private BindValueUtils() {
    }

    public static String bindValueToString(String[] bindValueArray, int limit) {
        if (bindValueArray == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder(32);
        final int length = bindValueArray.length;
        final int end = length - 1;
        for (int i = 0; i < length; i++) {
            if (sb.length() >= limit) {
                // Appending omission postfix makes generating binded sql difficult. But without this, we cannot say if it's omitted or not.
                appendLength(sb, length);
                break;
            }
            StringUtils.appendDrop(sb, bindValueArray[i], limit);
            if (i < end) {
                sb.append(", ");
            }

        }
        return sb.toString();
    }

    private static void appendLength(StringBuilder sb, int length) {
        sb.append("...(");
        sb.append(length);
        sb.append(')');
    }

    public static String bindValueToString(String[] stringArray) {
        return bindValueToString(stringArray, Integer.MAX_VALUE);
    }
}
