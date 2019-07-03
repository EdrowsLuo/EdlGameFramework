package com.edlplan.framework.utils.serialize;

public interface FastSerializable {

    short getFactoryId();

    int getObjectSize();

    void write(SerializableStream stream);

    void read(SerializableStream stream);

}
