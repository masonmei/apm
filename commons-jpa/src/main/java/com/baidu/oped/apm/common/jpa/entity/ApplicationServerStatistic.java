package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Application Server statistic map.
 *
 * Created by mason on 9/28/15.
 */
@Entity
@Table(name = "apm_application_server_statistic", indexes = {
        @Index(name = "app_server_statistic_point_unique", columnList = "app_id,period,timestamp", unique = true)})
public class ApplicationServerStatistic extends AbstractServerStatistic {
    @Basic
    @Column(name = "app_id", nullable = false, insertable = true, updatable = true)
    private Long appId;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
