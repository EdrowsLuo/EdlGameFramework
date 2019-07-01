package com.edlplan.framework.utils;

import com.edlplan.framework.utils.interfaces.Constructor;
import com.edlplan.framework.utils.interfaces.Copyable;

import java.util.Arrays;
import java.util.Collection;

public class StructArray<T> {

    private Object[] ary;

    private int limit, offset;

    private Factory<T> constructor;

    public StructArray(int size, Factory<T> constructor) {
        ary = new Object[size];
        this.constructor = constructor;
    }

    public StructArray(Factory<T> constructor) {
        this(16, constructor);
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        return (T) ary[index];
    }

    @SuppressWarnings("unchecked")
    public T add() {
        if (offset >= ary.length) {
            ary = Arrays.copyOf(ary, ary.length * 3 / 2 + 1);
        }
        offset++;
        if (limit < offset) {
            ary[limit++] = constructor.create();
        }
        return (T) ary[offset - 1];
    }

    public void add(T t) {
        if (offset >= ary.length) {
            ary = Arrays.copyOf(ary, ary.length * 3 / 2 + 1);
        }
        ary[offset++] = t;
        if (limit < offset) {
            limit++;
        }
    }

    public void addAll(Collection<T> collection) {
        for (T t : collection) {
            add(t);
        }
    }

    public int size() {
        return offset;
    }

    public void clear() {
        offset = 0;
    }


}
