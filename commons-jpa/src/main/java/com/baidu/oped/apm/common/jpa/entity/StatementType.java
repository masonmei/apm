package com.baidu.oped.apm.common.jpa.entity;

public enum StatementType {
    INSERT,
    DELETE,
    UPDATE,
    SELECT;

    public static StatementType get(String name) {
        for (StatementType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}
