package com.edlplan.framework.utils.interfaces;

@FunctionalInterface
public interface Getter<T> {
    T get();

    default Getter<T> runOnFinal(Consumer<T> consumer) {
        return () -> {
            T t = this.get();
            consumer.consume(t);
            return t;
        };
    }
}
