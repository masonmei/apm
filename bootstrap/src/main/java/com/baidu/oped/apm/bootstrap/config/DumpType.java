package com.baidu.oped.apm.bootstrap.config;

/**
 * class DumpType
 *
 * @author meidongxu@baidu.com
 */
public enum DumpType {
    //  NONE(-1),  comment out becasue of duplicated configuration.
    ALWAYS(0), EXCEPTION(1);

    private int code;

    private DumpType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
