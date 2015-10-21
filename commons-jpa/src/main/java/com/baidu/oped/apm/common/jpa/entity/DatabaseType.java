package com.baidu.oped.apm.common.jpa.entity;

/**
 * Created by mason on 10/20/15.
 */
public enum DatabaseType {
    DATABASE("Database"),
    MONGODB("MongoDB"),
    REDIS("Redis"),
    MEMCACHED("Memcached");

    private String label;

    DatabaseType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
