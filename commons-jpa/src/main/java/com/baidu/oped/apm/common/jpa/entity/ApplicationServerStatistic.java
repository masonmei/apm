package com.baidu.oped.apm.common.jpa.entity;

import com.google.common.base.MoreObjects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Application Server statistic map.
 * <p>
 * Created by mason on 9/28/15.
 */
@Entity
@Table(name = "apm_application_server_statistic", indexes = {
        @Index(name = "app_server_statistic_point_unique", columnList = "app_id,period,`timestamp`", unique = true)})
public class ApplicationServerStatistic extends AbstractServerStatistic {
    private static final long serialVersionUID = -6888969876356148718L;
    @Basic
    @Column(name = "app_id", nullable = false, insertable = true, updatable = true)
    private Long appId;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("appId", appId)
                .add("parent", super.toString())
                .toString();
    }
}
