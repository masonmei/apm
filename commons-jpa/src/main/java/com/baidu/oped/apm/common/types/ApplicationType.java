package com.baidu.oped.apm.common.types;

/**
 * Created by mason on 8/27/15.
 */
public enum  ApplicationType {
    JAVA("java"),
    PHP("php");

    private String key;

    ApplicationType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static ApplicationType getByKey(String key){
        for (ApplicationType applicationType : values()) {
            if(applicationType.getKey().equalsIgnoreCase(key)){
                return applicationType;
            }
        }
        throw new IllegalArgumentException("unknown key.");
    }

}
