package com.baidu.oped.apm.mvc.vo;

import com.google.common.base.MoreObjects;

/**
 * Created by mason on 10/8/15.
 */
public class CallRecordVo {
    private final int tab;
    private final int id;
    private final int parentId;

    private final boolean method;

    private final String title;
    private String simpleClassName = "";
    private String fullApiDescription = "";

    private final String arguments;
    private final long begin;
    private final long elapsed;
    private final long executionMillis;

    private final Long agentId;
    private final Long appId;
    private final Long instanceId;
    private final String appName;
    private final String instanceName;

    private final String destination;
    private final boolean hasException;

    public CallRecordVo(int tab, int id, int parentId, boolean method, String title, String arguments, long begin,
            long elapsed, long executionMillis, Long agentId, Long appId, Long instanceId, String appName,
            String instanceName, String destination, boolean hasException) {
        this.tab = tab;
        this.id = id;
        this.parentId = parentId;
        this.method = method;
        this.title = title;
        this.arguments = arguments;
        this.begin = begin;
        this.elapsed = elapsed;
        this.executionMillis = executionMillis;
        this.agentId = agentId;
        this.appId = appId;
        this.instanceId = instanceId;
        this.appName = appName;
        this.instanceName = instanceName;
        this.destination = destination;
        this.hasException = hasException;
    }

    public int getTab() {
        return tab;
    }

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }

    public boolean isMethod() {
        return method;
    }

    public String getTitle() {
        return title;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public void setSimpleClassName(String simpleClassName) {
        this.simpleClassName = simpleClassName;
    }

    public String getFullApiDescription() {
        return fullApiDescription;
    }

    public void setFullApiDescription(String fullApiDescription) {
        this.fullApiDescription = fullApiDescription;
    }

    public String getArguments() {
        return arguments;
    }

    public long getBegin() {
        return begin;
    }

    public long getElapsed() {
        return elapsed;
    }

    public long getExecutionMillis() {
        return executionMillis;
    }

    public Long getAgentId() {
        return agentId;
    }

    public Long getAppId() {
        return appId;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public String getAppName() {
        return appName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public String getDestination() {
        return destination;
    }

    public boolean isHasException() {
        return hasException;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("tab", tab).add("id", id).add("parentId", parentId)
                .add("method", method).add("title", title).add("simpleClassName", simpleClassName)
                .add("fullApiDescription", fullApiDescription).add("arguments", arguments).add("begin", begin)
                .add("elapsed", elapsed).add("executionMillis", executionMillis).add("agentId", agentId)
                .add("appId", appId).add("instanceId", instanceId).add("appName", appName)
                .add("instanceName", instanceName).add("destination", destination).add("hasException", hasException)
                .toString();
    }
}
