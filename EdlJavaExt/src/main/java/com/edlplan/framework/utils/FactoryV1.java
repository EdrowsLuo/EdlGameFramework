package com.edlplan.framework.utils;

@FunctionalInterface
public interface FactoryV1<T, V> {
    T create(V v);
}
