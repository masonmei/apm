package com.baidu.oped.apm.mvc.vo.callstack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

/**
 * Created by mason on 10/13/15.
 */
public class TraceCallTree implements CallTree {
    private static final int MIN_DEPTH = -1;
    private static final int LEVEL_DEPTH = -1;
    private static final int ROOT_DEPTH = 0;

    private CallTreeNode root;
    private CallTreeNode cursor;

    public TraceCallTree(final CallTreeAlign align) {
        this.root = new CallTreeNode(null, align);
        this.cursor = this.root;
    }

    @Override
    public CallTreeNode getRoot() {
        return root;
    }

    @Override
    public CallTreeIterator iterator() {
        return new CallTreeIterator(root);
    }

    @Override
    public boolean isEmpty() {
        return root.getAlign() == null;
    }

    @Override
    public void add(CallTree callTree) {
        final CallTreeNode node = callTree.getRoot();
        if (node == null) {
            return;
        }

        if (!cursor.hasChild()) {
            node.setParent(cursor);
            cursor.setChild(node);
            return;
        }

        CallTreeNode sibling = findLastSibling(cursor.getChild());
        node.setParent(sibling.getParent());
        sibling.setSibling(node);
    }

    @Override
    public void add(int depth, final CallTreeAlign align) {
//        if (depth < MIN_DEPTH || depth == ROOT_DEPTH) {
//            throw new IllegalArgumentException(format("invalid depth. depth=%d, cursor=%s, align=%s", depth, cursor, align));
//        }

//        if (hasCorrupted(align)) {
//            throw new CorruptedTreeNodeException(format("corrupted event. depth=%d, cursor=%s, align=%s", depth, cursor, align));
//        }

        if (depth == LEVEL_DEPTH || depth == cursor.getDepth()) {
            // validate
            if (cursor.isRoot()) {
                throw new IllegalArgumentException(format("invalid depth. depth=%d, cursor=%s, align=%s", depth, cursor, align));
            }

            CallTreeNode sibling = findLastSibling(cursor);
            sibling.setSibling(align);
            cursor = sibling.getSibling();
            return;
        }

        // greater
        if (depth > cursor.getDepth()) {
            // validate
            if (depth > cursor.getDepth() + 1) {
                throw new IllegalArgumentException(format("invalid depth. depth=%d, cursor=%s, align=%s", depth, cursor, align));
            }

            if (!cursor.hasChild()) {
                cursor.setChild(align);
                cursor = cursor.getChild();
                return;
            }

            CallTreeNode sibling = findLastSibling(cursor.getChild());
            sibling.setSibling(align);
            cursor = sibling.getSibling();
            return;
        }

        // lesser
        if (cursor.getDepth() - depth <= ROOT_DEPTH) {
            throw new IllegalArgumentException("invalid depth. depth=" + depth + ", cursor=" + cursor + ", align=" + align);
        }

        final CallTreeNode node = findUpperLevelLastSibling(depth, cursor);
        node.setSibling(align);
        cursor = node.getSibling();
    }

    private CallTreeNode findUpperLevelLastSibling(int level, CallTreeNode node) {
        final CallTreeNode parent = findUpperLevel(level, node);
        return findLastSibling(parent);
    }

    private boolean hasCorrupted(CallTreeAlign align) {
        if (align.isTrace()) {
            return false;
        }

        if (cursor.getAlign().isTrace()) {
            return align.getTraceEvent().getSequence() != 0;
        }

        return cursor.getAlign().getTraceEvent().getSequence() + 1 != align.getTraceEvent().getSequence();
    }

    private CallTreeNode findUpperLevel(int level, CallTreeNode node) {
        CallTreeNode parent = node.getParent();
        while (parent.getDepth() != level) {
            parent = parent.getParent();
        }
        return parent;
    }


    private CallTreeNode findLastSibling(CallTreeNode node) {
        CallTreeNode lastSibling = node;
        while (lastSibling.getSibling() != null) {
            lastSibling = lastSibling.getSibling();
        }
        return lastSibling;
    }

    @Override
    public void sort() {
        travel(root);
    }

    private void travel(CallTreeNode node) {
        sortChildSibling(node);
        if(node.hasChild()){
            travel(node.getChild());
        }

        if(node.hasSibling()){
            travel(node.getSibling());
        }
    }

    private void sortChildSibling(CallTreeNode parent) {
        if (!parent.hasChild() || !parent.getChild().hasSibling()) {
            return;
        }

        final List<CallTreeNode> nodes = getChildSiblingNodes(parent);
        if(nodes == null) {
            return;
        }

        Collections.sort(nodes,
                (source, target) -> (int) (source.getAlign().getStartTime() - target.getAlign().getStartTime()));

        // reform sibling
        CallTreeNode prev = null;
        for (CallTreeNode node : nodes) {
            final CallTreeNode reset = null;
            node.setSibling(reset);
            if (prev == null) {
                parent.setChild(node);
                prev = node;
            } else {
                prev.setSibling(node);
                prev = node;
            }
        }
    }

    private List<CallTreeNode> getChildSiblingNodes(CallTreeNode parent) {
        final List<CallTreeNode> nodes = new ArrayList<>();
        boolean span = false;
        CallTreeNode node = parent.getChild();
        nodes.add(node);
        if(node.getAlign().isTrace()) {
            span = true;
        }
        while (node.hasSibling()) {
            node = node.getSibling();
            nodes.add(node);
            if(node.getAlign().isTrace()) {
                span = true;
            }
        }

        if(nodes.size() < 2 || !span) {
            return null;
        }

        return nodes;
    }
}
