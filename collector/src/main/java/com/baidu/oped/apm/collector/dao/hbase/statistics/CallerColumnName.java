
package com.baidu.oped.apm.collector.dao.hbase.statistics;

import com.baidu.oped.apm.common.util.ApplicationMapStatisticsUtils;

/**
 * class CallerColumnName 
 *
 * @author meidongxu@baidu.com
 */
public class CallerColumnName implements ColumnName {
    private short callerServiceType;
    private String callerApplicationName;
    // called or calling host
    private String callHost;
    private short columnSlotNumber;

    // WARNING - cached hash value should not be included for equals/hashCode
    private int hash;

    private long callCount;

    public CallerColumnName(short callerServiceType, String callerApplicationName, String callHost, short columnSlotNumber) {
        if (callerApplicationName == null) {
            throw new NullPointerException("callerApplicationName must not be null");
        }
        if (callHost == null) {
            throw new NullPointerException("callHost must not be null");
        }
        this.callerServiceType = callerServiceType;
        this.callerApplicationName = callerApplicationName;
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
        return ApplicationMapStatisticsUtils.makeColumnName(callerServiceType, callerApplicationName, callHost, columnSlotNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CallerColumnName that = (CallerColumnName) o;

        if (callerServiceType != that.callerServiceType) return false;
        if (columnSlotNumber != that.columnSlotNumber) return false;
        if (callerApplicationName != null ? !callerApplicationName.equals(that.callerApplicationName) : that.callerApplicationName != null) return false;
        if (callHost != null ? !callHost.equals(that.callHost) : that.callHost != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        // take care when modifying this method - contains hashCodes for hbasekeys 
        if (hash != 0) {
            return hash;
        }
        int result = (int) callerServiceType;
        result = 31 * result + (callerApplicationName != null ? callerApplicationName.hashCode() : 0);
        result = 31 * result + (callHost != null ? callHost.hashCode() : 0);
        result = 31 * result + (int) columnSlotNumber;
        hash = result;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CallerColumnName{");
        sb.append("callerServiceType=").append(callerServiceType);
        sb.append(", callerApplicationName='").append(callerApplicationName).append('\'');
        sb.append(", callHost='").append(callHost).append('\'');
        sb.append(", columnSlotNumber=").append(columnSlotNumber);
        sb.append(", callCount=").append(callCount);
        sb.append('}');
        return sb.toString();
    }
}
