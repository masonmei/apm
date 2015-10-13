package com.baidu.oped.apm.common.jpa.entity;

import com.google.common.base.MoreObjects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Instance Server statistic map.
 * <p>
 * Created by mason on 9/28/15.
 */
@Entity
@Table(name = "apm_instance_server_statistic", indexes = {
        @Index(name = "app_instance_statistic_point_unique", columnList = "instance_id,period,timestamp", unique =
                true)})
public class InstanceServerStatistic extends AbstractServerStatistic {

    private static final long serialVersionUID = -5695051454818240712L;
    @Basic
    @Column(name = "instance_id", nullable = false, insertable = true, updatable = true)
    private Long instanceId;

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("instanceId", instanceId)
                .add("parent", super.toString())
                .toString();
    }
}
