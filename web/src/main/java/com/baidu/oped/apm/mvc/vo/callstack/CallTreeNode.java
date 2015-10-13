package com.baidu.oped.apm.mvc.vo.callstack;

import com.google.common.base.MoreObjects;

/**
 * Created by mason on 10/13/15.
 */
public class CallTreeNode {
    private CallTreeNode parent;
    private CallTreeNode child;
    private CallTreeNode sibling;
    private final CallTreeAlign align;

    public CallTreeNode(final CallTreeNode parent, CallTreeAlign align) {
        this.parent = parent;
        this.align = align;
    }

    public CallTreeNode getParent() {
        return parent;
    }

    public void setParent(CallTreeNode parent) {
        this.parent = parent;
    }

    public boolean hasChild() {
        return this.child != null;
    }

    public CallTreeNode getChild() {
        return child;
    }

    public void setChild(CallTreeNode child) {
        this.child = child;
    }

    public void setChild(final CallTreeAlign align) {
        this.child = new CallTreeNode(this, align);
    }

    public boolean hasSibling() {
        return this.sibling != null;
    }

    public CallTreeNode getSibling() {
        return sibling;
    }

    public void setSibling(CallTreeNode sibling) {
        this.sibling = sibling;
    }

    public void setSibling(final CallTreeAlign align) {
        this.sibling = new CallTreeNode(parent, align);
    }

    public CallTreeAlign getAlign() {
        return align;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public int getDepth() {
        if (isRoot()) {
            return 0;
        }
        return parent.getDepth() + 1;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("parent", parent)
                .add("child", child)
                .add("sibling", sibling)
                .add("align", align)
                .toString();
    }
}
