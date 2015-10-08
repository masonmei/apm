package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Created by mason on 9/20/15.
 */
@Entity
@Table(name = "apm_user_conf", indexes = {@Index(name = "user_conf_unique",
                                                 columnList = "user_id,conf_key", unique = true)})

public class UserConfig extends AbstractPersistable<Long> {
    private static final long serialVersionUID = -4406053716949676814L;
    @Basic
    @Column(name = "user_id", nullable = false, insertable = true, updatable = true)
    private String userId;
    @Basic
    @Column(name = "conf_key", nullable = false, insertable = true, updatable = false)
    private String confKey;
    @Basic
    @Column(name = "description", nullable = true, insertable = true, updatable = true)
    private String description;

    @Basic
    @Column(name = "conf_value", unique = false, insertable = true, updatable = true)
    private String confValue;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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
}
