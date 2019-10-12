package com.edlplan.framework.utils;

import com.edlplan.framework.utils.interfaces.Consumer;
import com.edlplan.framework.utils.interfaces.Getter;

/**
 * 当一个成员变量是不一定使用或者不需要在初始化时初始化的时候可以用Lazy来包装
 * @param <T>
 */
public abstract class Lazy<T> {

    public static void main(String[] args) {
        Lazy<Integer> lazy = Lazy.create(() -> 100).runOnCreate(System.out::println);
        System.out.println("then " + lazy.get());
    }


    private T data;

    protected abstract T initial();

    public T get() {
        if (data == null) {
            data = initial();
        }
        return data;
    }

    public boolean isEmpty() {
        return data == null;
    }

    public static <T> LazyGetter<T> create(Getter<T> getter) {
        return new LazyGetter<>(getter);
    }

    public static class LazyGetter<T> extends Lazy<T> {

        private Getter<T> getter;

        public LazyGetter(Getter<T> getter) {
            this.getter = getter;
        }

        public Getter<T> getGetter() {
            return getter;
        }

        public LazyGetter<T> runOnCreate(Consumer<T> consumer) {
            if (!isEmpty()) {
                throw new IllegalStateException("you can only add consumer to lazy loader when it's not initial");
            }
            getter = getter.runOnFinal(consumer);
            return this;
        }

        @Override
        protected T initial() {
            return getter.get();
        }
    }
}

