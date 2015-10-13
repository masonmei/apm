package com.baidu.oped.apm.mvc.vo.callstack;

/**
 * Created by mason on 10/13/15.
 */
public interface CallTree extends Iterable<CallTreeNode> {

    CallTreeNode getRoot();

    CallTreeIterator iterator();

    boolean isEmpty();

    void add(CallTree callTree);

    void add(int depth, CallTreeAlign align);

    void sort();
}
