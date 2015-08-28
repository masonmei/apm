package com.baidu.oped.apm.common.jpa.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Created by mason on 8/27/15.
 */
@Entity
@Table(name = "apm_application", schema = "", catalog = "apm")
public class Application extends AbstractPersistable<Long> implements Serializable {

    @Basic
    @Column(name = "app_name", nullable = false, insertable = true, updatable = true, length = 128)
    private String appName;

    @Basic
    @Column(name = "app_type", nullable = true, insertable = true, updatable = true, length = 64)
    private String appType;

    @Basic
    @Column(name = "user_id", nullable = true, insertable = true, updatable = true, length = 128)
    private String userId;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
