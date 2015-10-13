package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.google.common.base.MoreObjects;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Created by mason on 9/20/15.
 */
@Entity
@Table(name = "apm_sys_conf", indexes = {@Index(name = "sys_conf_unique", columnList = "conf_key", unique = true)})
public class SystemConfig extends AbstractPersistable<Long> {
    private static final long serialVersionUID = -647322328583911424L;
    @Basic
    @Column(name = "conf_key", nullable = false, insertable = true, updatable = false)
    private String confKey;
    @Basic
    @Column(name = "description", nullable = true, insertable = true, updatable = true)
    private String description;
    @Basic
    @Column(name = "conf_value", nullable = false, insertable = true, updatable = true)
    private String confValue;

    public String getConfKey() {
        return confKey;
    }

    public void setConfKey(String confKey) {
        this.confKey = confKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConfValue() {
        return confValue;
    }

    public void setConfValue(String confValue) {
        this.confValue = confValue;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("confKey", confKey)
                .add("description", description)
                .add("confValue", confValue)
                .toString();
    }
}
