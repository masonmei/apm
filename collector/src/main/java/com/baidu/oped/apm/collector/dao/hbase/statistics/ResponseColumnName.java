
package com.baidu.oped.apm.collector.dao.hbase.statistics;

import com.baidu.oped.apm.common.util.ApplicationMapStatisticsUtils;

/**
 * class ResponseColumnName 
 *
 * @author meidongxu@baidu.com
 */
public class ResponseColumnName implements ColumnName {

    private String agentId;
    private short columnSlotNumber;

 // WARNING - cached hash value should not be included for equals/hashCode
    private int hash;

    private long callCount;

    public ResponseColumnName(String agentId, short columnSlotNumber) {
        if (agentId == null) {
            throw new NullPointerException("agentId must not be null");
        }
        this.agentId = agentId;
        this.columnSlotNumber = columnSlotNumber;
    }

    public long getCallCount() {
        return callCount;
    }

    public void setCallCount(long callCount) {
        this.callCount = callCount;
    }

    public byte[] getColumnName() {
        return ApplicationMapStatisticsUtils.makeColumnName(agentId, columnSlotNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResponseColumnName that = (ResponseColumnName) o;

        if (columnSlotNumber != that.columnSlotNumber) return false;
        if (!agentId.equals(that.agentId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        // take care when modifying this method - contains hashCodes for hbasekeys
        if (hash != 0) {
            return hash;
        }
        int result = agentId.hashCode();
        result = 31 * result + (int) columnSlotNumber;
        hash = result;
        return result;
    }

    @Override
    public String toString() {
        return "ResponseColumnName{" +
                "agentId='" + agentId + '\'' +
                ", columnSlotNumber=" + columnSlotNumber +
                ", callCount=" + callCount +
                '}';
    }
}
