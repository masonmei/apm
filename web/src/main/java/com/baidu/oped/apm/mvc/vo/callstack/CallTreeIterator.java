package com.baidu.oped.apm.mvc.vo.callstack;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mason on 10/13/15.
 */
public class CallTreeIterator implements Iterator<CallTreeNode> {

    private List<CallTreeNode> nodes = new LinkedList<>();
    private int index = -1;

    public CallTreeIterator(final CallTreeNode root) {
        if (root == null) {
            return;
        }
        populate(root);
        index = -1;
    }

    private void populate(CallTreeNode node) {
        nodes.add(node);
        index++;

    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public CallTreeNode next() {
        return null;
    }
}
