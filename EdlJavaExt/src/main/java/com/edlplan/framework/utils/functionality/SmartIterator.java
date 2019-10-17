package com.edlplan.framework.utils.functionality;

import com.edlplan.framework.utils.R;
import com.edlplan.framework.utils.interfaces.Function;
import com.edlplan.framework.utils.interfaces.Getter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    static <T> SmartIterator<T> ofArray(T[] ary) {
        return new SmartIterator<T>() {

            int c = 0;

            @Override
            public boolean hasNext() {
                return c < ary.length;
            }

            @Override
            public T next() {
                c++;
                return ary[c - 1];
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

    default List<T> collectAllAsList() {
        ArrayList<T> list = new ArrayList<>();
        while (hasNext()) {
            list.add(next());
        }
        return list;
    }

    default T[] collectAllAsArray(T[] ary) {
        return collectAllAsList().toArray(ary);
    }

    default byte[] collectAllAsByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (hasNext()) {
            T t = next();
            byteArrayOutputStream.write((Integer) t);
        }
        return byteArrayOutputStream.toByteArray();
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
                return nextCache != null;
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

    default <V> SmartIterator<V> mixElements(Function<T, SmartIterator<V>> function) {
        return mix(applyFunction(function));
    }

    static  <T> SmartIterator<T> mix(SmartIterator<SmartIterator<T>> smartIterators) {
        return new SmartIterator<T>() {

            SmartIterator<T> current = smartIterators.hasNext() ? smartIterators.next() : null;

            @Override
            public boolean hasNext() {
                if (current != null) {
                    if (current.hasNext()) {
                        return true;
                    } else {
                        if (smartIterators.hasNext()) {
                            current = smartIterators.next();
                            return hasNext();
                        } else {
                            current = null;
                            return false;
                        }
                    }
                }
                return false;
            }

            @Override
            public T next() {
                while (current == null || !current.hasNext()) {
                    if (smartIterators.hasNext()) {
                        current = smartIterators.next();
                    } else {
                        throw new IndexOutOfBoundsException("no more elements");
                    }
                }
                return current.next();
            }
        };
    }

}
