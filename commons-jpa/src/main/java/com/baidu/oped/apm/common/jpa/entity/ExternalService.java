package com.baidu.oped.apm.common.jpa.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.google.common.base.MoreObjects;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Created by mason on 8/27/15.
 */
@Entity
@Table(name = "apm_external_service", indexes = {
        @Index(name = "external_service_unique", columnList = "app_id,instance_id,destination_id,url", unique = true)})
public class ExternalService extends AbstractPersistable<Long> implements Serializable {

    private static final long serialVersionUID = -3720516128778661519L;
    @Basic
    @Column(name = "app_id", nullable = false, insertable = true, updatable = true)
    private Long appId;

    @Basic
    @Column(name = "instance_id", nullable = false, insertable = true, updatable = true)
    private Long instanceId;

    @Basic
    @Column(name = "destination_id", nullable = false, insertable = true, updatable = true, length = 256)
    private String destinationId;

    @Basic
    @Column(name = "url", nullable = false, insertable = true, updatable = true, length = 512)
    private String url;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("appId", appId)
                .add("instanceId", instanceId)
                .add("destinationId", destinationId)
                .add("url", url)
                .toString();
    }
}
