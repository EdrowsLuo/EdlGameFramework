package com.edlplan.framework.utils.serialize;

import com.edlplan.framework.utils.CharArray;

import java.nio.ByteBuffer;

public abstract class SerializableStream {

    public abstract void putByte(byte v);

    public abstract void putInt(int v);

    public abstract void putShort(short v);

    public abstract void putFloat(float v);

    public abstract void putDouble(double v);

    public abstract void putBoolean(boolean v);

    public abstract void putString(String v);

    public abstract void putObject(FastSerializable v);

    public  <T extends Enum> void putEnum(T v) {
        putByte((byte) v.ordinal());
    }

}
