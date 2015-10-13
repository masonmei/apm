package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.google.common.base.MoreObjects;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Created by mason on 8/27/15.
 */
@Entity
@Table(name = "apm_instance")
public class Instance extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 5660693988117311011L;
    @Basic
    @Column(name = "app_id", nullable = false, insertable = true, updatable = true)
    private Long appId;

    @Basic
    @Column(name = "port", nullable = true, insertable = true, updatable = true)
    private Integer port;

    @Basic
    @Column(name = "host", nullable = true, insertable = true, updatable = true, length = 128)
    private String host;

    @Basic
    @Column(name = "ip", nullable = true, insertable = true, updatable = true, length = 128)
    private String ip;

    @Basic
    @Column(name = "pid", nullable = true, insertable = true, updatable = true)
    private Integer pid;

    @Basic
    @Column(name = "start_time", nullable = true, insertable = true, updatable = true)
    private Long startTime;

    @Basic
    @Column(name = "instance_type", nullable = true, insertable = true, updatable = true)
    private Integer instanceType;

    @Basic
    @Column(name = "args", nullable = true, insertable = true, updatable = true, length = 2048)
    private String args;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Integer getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(Integer instanceType) {
        this.instanceType = instanceType;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("args", args)
                .add("appId", appId)
                .add("port", port)
                .add("host", host)
                .add("ip", ip)
                .add("pid", pid)
                .add("startTime", startTime)
                .add("instanceType", instanceType)
                .toString();
    }
}
