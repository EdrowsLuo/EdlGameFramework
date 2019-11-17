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

    public Lazy<T> runOnCreate(Consumer<T> consumer) {
        if (!isEmpty()) {
            throw new IllegalStateException("you can only add consumer to lazy loader when it's not initial");
        }
        Lazy<T> p = this;
        return new Lazy<T>() {
            @Override
            protected T initial() {
                T t = p.initial();
                consumer.consume(t);
                return t;
            }
        };
    }

    public Lazy<T> runBeforeCreate(Runnable runnable) {
        if (!isEmpty()) {
            throw new IllegalStateException("you can only add runnable to lazy loader when it's not initial");
        }
        Lazy<T> p = this;
        return new Lazy<T>() {
            @Override
            protected T initial() {
                runnable.run();
                T t = p.initial();
                return t;
            }
        };
    }

    public Lazy<T> copy() {
        Lazy<T> proto = this;
        return new Lazy<T>() {
            @Override
            protected T initial() {
                return proto.initial();
            }
        };
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

        @Override
        protected T initial() {
            return getter.get();
        }
    }
}

