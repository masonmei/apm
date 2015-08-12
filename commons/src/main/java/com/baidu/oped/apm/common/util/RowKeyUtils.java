
package com.baidu.oped.apm.common.util;

import static com.baidu.oped.apm.common.PinpointConstants.AGENT_NAME_MAX_LEN;
import static com.baidu.oped.apm.common.util.BytesUtils.INT_BYTE_LENGTH;
import static com.baidu.oped.apm.common.util.BytesUtils.LONG_BYTE_LENGTH;

/**
 * class RowKeyUtils 
 *
 * @author meidongxu@baidu.com
 */
public final class RowKeyUtils {
    private RowKeyUtils() {
    }

    public static byte[] concatFixedByteAndLong(byte[] fixedBytes, int maxFixedLength, long l) {
        if (fixedBytes == null) {
            throw new NullPointerException("fixedBytes must not null");
        }
        if (fixedBytes.length > maxFixedLength) {
            throw new IndexOutOfBoundsException("fixedBytes.length too big. length:" + fixedBytes.length);
        }
        byte[] rowKey = new byte[maxFixedLength + LONG_BYTE_LENGTH];
        BytesUtils.writeBytes(rowKey, 0, fixedBytes);
        BytesUtils.writeLong(l, rowKey, maxFixedLength);
        return rowKey;
    }


    public static byte[] getMetaInfoRowKey(String agentId, long agentStartTime, int keyCode) {
        if (agentId == null) {
            throw new NullPointerException("agentId must not be null");
        }

        final byte[] agentBytes = BytesUtils.toBytes(agentId);
        if (agentBytes.length > AGENT_NAME_MAX_LEN) {
            throw new IndexOutOfBoundsException("agent.length too big. agent:" + agentId + " length:" + agentId.length());
        }

        final byte[] buffer = new byte[AGENT_NAME_MAX_LEN + LONG_BYTE_LENGTH + INT_BYTE_LENGTH];
        BytesUtils.writeBytes(buffer, 0, agentBytes);

        long reverseCurrentTimeMillis = TimeUtils.reverseTimeMillis(agentStartTime);
        BytesUtils.writeLong(reverseCurrentTimeMillis, buffer, AGENT_NAME_MAX_LEN);

        BytesUtils.writeInt(keyCode, buffer, AGENT_NAME_MAX_LEN + LONG_BYTE_LENGTH);
        return buffer;
    }


}
