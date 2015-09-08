package com.baidu.oped.apm.common.jpa.entity;

/**
 * Created by mason on 8/27/15.
 */
public enum ServiceType {
    WEB("WEB事务"),
    DB("数据库"),
    EXTERNAL("外部服务"),
    BACKGROUND("后台任务"),
    CACHE("缓存");

    private String description;

    ServiceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
