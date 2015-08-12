package com.baidu.oped.apm.bootstrap.context;

/**
 * class StackOperation
 *
 * @author meidongxu@baidu.com
 */
public interface StackOperation {

    public static final int DEFAULT_STACKID = -1;
    public static final int ROOT_STACKID = 0;

    void traceBlockBegin();

    void traceBlockBegin(int stackId);

    // TODO consider to make a interface as below
    // traceRootBlockBegin

    void traceRootBlockEnd();

    void traceBlockEnd();

    void traceBlockEnd(int stackId);
}
