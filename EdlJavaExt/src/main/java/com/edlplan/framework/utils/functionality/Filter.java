package com.edlplan.framework.utils.functionality;

@FunctionalInterface
public interface Filter<T> {
    boolean accept(T t);
}
