package com.baidu.oped.apm.mvc.vo;

import java.util.List;

/**
 * Created by mason on 10/8/15.
 */
public class CallStackVo {
    private Long appId;
    private String appName;
    private Long instanceId;
    private String instanceName;

    private Long beginTimestamp;
    private List<CallRecordVo> callRecords;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public Long getBeginTimestamp() {
        return beginTimestamp;
    }

    public void setBeginTimestamp(Long beginTimestamp) {
        this.beginTimestamp = beginTimestamp;
    }

    public List<CallRecordVo> getCallRecords() {
        return callRecords;
    }

    public void setCallRecords(List<CallRecordVo> callRecords) {
        this.callRecords = callRecords;
    }
}
