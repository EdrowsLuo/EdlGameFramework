package com.edlplan.framework.utils;

import com.edlplan.framework.utils.interfaces.Consumer;

import java.util.Stack;

public class Cache<T> {

    private final Object lock = new Object();

    private Factory<T> factory;

    private Stack<T> stack = new Stack<>();

    private int maxObjectCount = 100;

    private Consumer<T> clearUp;

    public Cache(Factory<T> factory, int maxObjectCount) {
        this.factory = factory;
        this.maxObjectCount = maxObjectCount;
    }

    public Cache(Factory<T> factory, Consumer<T> clearUp, int maxObjectCount) {
        this.factory = factory;
        this.maxObjectCount = maxObjectCount;
        this.clearUp = clearUp;
    }

    public boolean isFull() {
        return stack.size() >= maxObjectCount;
    }

    public void setClearUp(Consumer<T> clearUp) {
        this.clearUp = clearUp;
    }

    public T get() {
        synchronized (lock) {
            if (stack.isEmpty()) {
                return factory.create();
            } else {
                return stack.pop();
            }
        }
    }

    public void save(T t) {
        synchronized (lock) {
            if (stack.size() > maxObjectCount) {
                return;
            }
            if (clearUp!=null) clearUp.consume(t);
            stack.push(t);
        }
    }

}
