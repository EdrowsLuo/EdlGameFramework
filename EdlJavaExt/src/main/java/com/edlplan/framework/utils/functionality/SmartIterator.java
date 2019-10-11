package com.edlplan.framework.utils.functionality;

import com.edlplan.framework.utils.interfaces.Function;
import com.edlplan.framework.utils.interfaces.Getter;

import java.util.Iterator;

public interface SmartIterator<T> extends Iterator<T> {


    static <T> SmartIterator<T> wrap(Iterator<T> iterator) {
        return new SmartIterator<T>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return iterator.next();
            }
        };
    }

    static <T> SmartIterator<T> ofArray(int count, Function<Integer, T> getter) {
        return new SmartIterator<T>() {

            int c = 0;

            @Override
            public boolean hasNext() {
                return c < count;
            }

            @Override
            public T next() {
                c++;
                return getter.reflect(c - 1);
            }
        };
    }

    default T selectOne() {
        T tmp = null;
        while (hasNext()) {
            tmp = next();
            if (tmp != null) {
                return tmp;
            }
        }
        return tmp;
    }

    default SmartIterator<T> applyFilter(Filter<T> filter) {

        SmartIterator<T> t = this;

        return new SmartIterator<T>() {

            T nextCache;

            {
                next();
            }

            @Override
            public boolean hasNext() {
                return t.hasNext();
            }

            @Override
            public T next() {
                T r = nextCache;
                nextCache = null;
                T c;
                while (t.hasNext()) {
                    c = t.next();
                    if (filter.accept(c)) {
                        nextCache = c;
                        break;
                    }
                }
                return r;
            }

        };
    }

    default <V> SmartIterator<V> applyFunction(Function<T, V> function) {
        SmartIterator<T> t = this;
        return new SmartIterator<V>() {

            @Override
            public boolean hasNext() {
                return t.hasNext();
            }

            @Override
            public V next() {
                return function.reflect(t.next());
            }
        };
    }

}
