
package com.baidu.oped.apm.common.bo;

import com.baidu.oped.apm.common.PinpointConstants;
import com.baidu.oped.apm.common.util.BytesUtils;
import com.baidu.oped.apm.common.util.RowKeyUtils;
import com.baidu.oped.apm.common.util.TimeUtils;

/**
 * class ApiMetaDataBo 
 *
 * @author meidongxu@baidu.com
 */
public class ApiMetaDataBo {
    private String agentId;
    private long startTime;

    private int apiId;

    private String apiInfo;
    private int lineNumber;

    public ApiMetaDataBo() {
    }

    public ApiMetaDataBo(String agentId, long startTime, int apiId) {
        if (agentId == null) {
            throw new NullPointerException("agentId must not be null");
        }

        this.agentId = agentId;
        this.startTime = startTime;
        this.apiId = apiId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public int getApiId() {
        return apiId;
    }

    public void setApiId(int apiId) {
        this.apiId = apiId;
    }


    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getApiInfo() {
        return apiInfo;
    }

    public void setApiInfo(String apiInfo) {
        this.apiInfo = apiInfo;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void readRowKey(byte[] bytes) {
        this.agentId = BytesUtils.safeTrim(BytesUtils.toString(bytes, 0, PinpointConstants.AGENT_NAME_MAX_LEN));
        this.startTime = TimeUtils.recoveryTimeMillis(readTime(bytes));
        this.apiId = readKeyCode(bytes);
    }

    private static long readTime(byte[] rowKey) {
        return BytesUtils.bytesToLong(rowKey, PinpointConstants.AGENT_NAME_MAX_LEN);
    }

    private static int readKeyCode(byte[] rowKey) {
        return BytesUtils.bytesToInt(rowKey, PinpointConstants.AGENT_NAME_MAX_LEN + BytesUtils.LONG_BYTE_LENGTH);
    }

    public byte[] toRowKey() {
        return RowKeyUtils.getMetaInfoRowKey(this.agentId, this.startTime, this.apiId);
    }

    @Override
    public String toString() {
        return "ApiMetaDataBo{" +
                "agentId='" + agentId + '\'' +
                ", apiId=" + apiId +
                ", startTime=" + startTime +
                ", apiInfo='" + apiInfo + '\'' +
                ", lineNumber=" + lineNumber +
                '}';
    }

}
