package com.baidu.oped.apm.common.jpa.entity;

/**
 * Created by mason on 8/27/15.
 */
public enum ServiceType {
    WEB(""),
    DB(""),
    EXTERNAL(""),
    BACKGROUD(""),
    CACHE("");

    private String description;

    ServiceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
