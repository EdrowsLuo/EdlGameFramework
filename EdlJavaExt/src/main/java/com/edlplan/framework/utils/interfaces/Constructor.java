package com.edlplan.framework.utils.interfaces;

@FunctionalInterface
public interface Constructor<T> {
    T createNew();
}
