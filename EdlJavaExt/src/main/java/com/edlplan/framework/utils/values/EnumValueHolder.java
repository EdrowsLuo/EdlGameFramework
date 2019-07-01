package com.edlplan.framework.utils.values;

public interface EnumValueHolder<T extends Enum> extends ValueHolder {

    T get();

    void set(T v);

}
