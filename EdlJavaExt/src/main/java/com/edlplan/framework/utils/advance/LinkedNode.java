package com.edlplan.framework.utils.advance;

public final class LinkedNode<T> {

    public LinkedNode<T> pre;

    public LinkedNode<T> next;

    public T value;

    /**
     *
     */
    private void checkLeft() {
        if (pre != null) {
            pre.next = this;
        }
    }

    /**
     *
     */
    private void checkRight() {
        if (next != null) {
            next.pre = this;
        }
    }

    /**
     * 将这个node从列表中移除
     */
    public void removeFromList() {
        if (pre != null) {
            pre.next = next;
        }
        if (next != null) {
            next.pre = pre;
        }
        pre = null;
        next = null;
    }

    /**
     * 将一个node添加到这个node的下一个位置
     * @param node 要添加的node（这个node应该是单的）
     */
    public void insertToNext(LinkedNode<T> node) {
        if (!node.isSingle()) {
            throw new IllegalArgumentException("insert a node that is used in other place!");
        }
        if (next != null) {
            next.pre = node;
        }
        node.next = next;
        next = node;
        node.pre = this;
    }

    /**
     * 将一个node添加到这个node的前一个位置
     * @param node 要添加的node（这个node应该是单的）
     */
    public void insertToPrevious(LinkedNode<T> node) {
        if (!node.isSingle()) {
            throw new IllegalArgumentException("insert a node that is used in other place!");
        }
        if (pre != null) {
            pre.next = node;
        }
        node.pre = pre;
        pre = node;
        node.next = this;
    }


    /**
     * 解除前面的连接
     */
    public void unlinkPre() {
        if (pre != null) {
            pre.next = null;
            pre = null;
        }
    }

    /**
     * 解除与下一个的连接
     */
    public void unlinkNext() {
        if (next != null) {
            next.pre = null;
            next = null;
        }
    }

    public boolean isFirst() {
        return pre == null;
    }

    public boolean isLast() {
        return next == null;
    }

    public boolean isSingle() {
        return pre == null && next == null;
    }

    public LinkedNode<T> findFirst() {
        LinkedNode<T> node = this;
        while (node.pre != null) {
            node = node.pre;
        }
        return node;
    }

    public LinkedNode<T> findLast() {
        LinkedNode<T> node = this;
        while (node.next != null) {
            node = node.next;
        }
        return node;
    }

    public int calNextNodeCount() {
        int i = 0;
        LinkedNode<T> node = this;
        while (node.next != null) {
            node = node.next;
            i++;
        }
        return i;
    }

    public int calPreNodeCount() {
        int i = 0;
        LinkedNode<T> node = this;
        while (node.pre != null) {
            node = node.pre;
            i++;
        }
        return i;
    }

    public int calLength() {
        return calNextNodeCount() + calPreNodeCount() + 1;
    }
}
