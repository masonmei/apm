
package com.baidu.oped.apm.collector.dao.hbase.statistics;

import com.baidu.oped.apm.common.buffer.AutomaticBuffer;
import com.baidu.oped.apm.common.buffer.Buffer;

/**
 * class CalleeColumnName 
 *
 * @author meidongxu@baidu.com
 */
public class CalleeColumnName implements ColumnName {
    private final String callerAgentId;
    private final short calleeServiceType;
    private final String calleeApplicationName;
    // called or calling host
    private final String callHost;
    private final short columnSlotNumber;

    // WARNING - cached hash value should not be included for equals/hashCode
    private int hash;

    private long callCount;

    public CalleeColumnName(String callerAgentId, short calleeServiceType, String calleeApplicationName, String callHost, short columnSlotNumber) {
        if (callerAgentId == null) {
            throw new NullPointerException("callerAgentId must not be null");
        }
        if (calleeApplicationName == null) {
            throw new NullPointerException("calleeApplicationName must not be null");
        }
        if (callHost == null) {
            throw new NullPointerException("callHost must not be null");
        }
        this.callerAgentId = callerAgentId;
        this.calleeServiceType = calleeServiceType;
        this.calleeApplicationName = calleeApplicationName;
        this.callHost = callHost;
        this.columnSlotNumber = columnSlotNumber;
    }

    public long getCallCount() {
        return callCount;
    }

    public void setCallCount(long callCount) {
        this.callCount = callCount;
    }

    public byte[] getColumnName() {
        final Buffer buffer = new AutomaticBuffer(64);
        buffer.put(calleeServiceType);
        buffer.putPrefixedString(calleeApplicationName);
        buffer.putPrefixedString(callHost);
        buffer.put(columnSlotNumber);
        buffer.putPrefixedString(callerAgentId);
        return buffer.getBuffer();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalleeColumnName that = (CalleeColumnName) o;

        if (callCount != that.callCount) return false;
        if (calleeServiceType != that.calleeServiceType) return false;
        if (columnSlotNumber != that.columnSlotNumber) return false;
        if (!callHost.equals(that.callHost)) return false;
        if (!calleeApplicationName.equals(that.calleeApplicationName)) return false;
        if (!callerAgentId.equals(that.callerAgentId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        // take care when modifying this method - contains hashCodes for hbasekeys
        if (hash != 0) {
            return hash;
        }
        int result = callerAgentId.hashCode();
        result = 31 * result + (int) calleeServiceType;
        result = 31 * result + calleeApplicationName.hashCode();
        result = 31 * result + callHost.hashCode();
        result = 31 * result + (int) columnSlotNumber;
        result = 31 * result + hash;
        result = 31 * result + (int) (callCount ^ (callCount >>> 32));
        this.hash = result;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CalleeColumnName{");
        sb.append("callerAgentId=").append(callerAgentId);
        sb.append(", calleeServiceType=").append(calleeServiceType);
        sb.append(", calleeApplicationName='").append(calleeApplicationName).append('\'');
        sb.append(", callHost='").append(callHost).append('\'');
        sb.append(", columnSlotNumber=").append(columnSlotNumber);
        sb.append(", callCount=").append(callCount);
        sb.append('}');
        return sb.toString();
    }
}
