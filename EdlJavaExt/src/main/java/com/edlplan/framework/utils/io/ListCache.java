package com.edlplan.framework.utils.io;

import com.edlplan.framework.utils.Factory;
import com.edlplan.framework.utils.functionality.SmartIterator;

import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

public class ListCache<T, I, O> implements Iterable<T> {

    private static ObjectToCache<String, DataInput, DataOutput> stringToCache = new ObjectToCache<String, DataInput, DataOutput>() {
        @Override
        public void saveToCache(DataOutput out, String s) throws IOException {
            byte[] bytes = s.getBytes();
            out.writeInt(bytes.length);
            out.write(bytes);
        }

        @Override
        public String loadFromCache(DataInput in) throws IOException {
            try {
                int l = in.readInt();
                byte[] bytes = new byte[l];
                in.readFully(bytes);
                return new String(bytes);
            } catch (EOFException e) {
                return null;
            }
        }
    };

    private Factory<I> iFactory;

    private Factory<O> oFactory;

    private ObjectToCache<T, I, O> objectToCache;

    private O out;

    public ListCache(Factory<I> iFactory, Factory<O> oFactory, ObjectToCache<T, I, O> objectToCache) {
        this.objectToCache = objectToCache;
        this.iFactory = iFactory;
        this.oFactory = oFactory;
    }

    public void add(T t) throws IOException {
        if (out == null) {
            out = oFactory.create();
        }
        objectToCache.saveToCache(out, t);
    }

    public void closeOut() {
        if (out != null && out instanceof Closeable) {
            try {
                ((Closeable) out).close();
                out = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Iterator<T> iterator() {
        return openIterator();
    }

    private SmartIterator<T> openIterator() {
        try {
            I in = iFactory.create();
            return new SmartIterator<T>() {

                T cacheNext;

                {
                    try {
                        cacheNext = objectToCache.loadFromCache(in);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public boolean hasNext() {
                    return cacheNext != null;
                }

                @Override
                public T next() {
                    T n = cacheNext;
                    try {
                        cacheNext = objectToCache.loadFromCache(in);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return n;
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static <T> ListCache<T, DataInput, DataOutput> openTmpFileListCache(ObjectToCache<T, DataInput, DataOutput> objectToCache) throws IOException {
        File tmp = File.createTempFile((new Random().nextLong()) + "", ".listcache");
        tmp.deleteOnExit();
        return new ListCache<>(
                () -> {
                    try {
                        return new DataInputStream(new FileInputStream(tmp));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                },
                () -> {
                    try {
                        return new DataOutputStream(new FileOutputStream(tmp));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                },
                objectToCache);
    }

    public static ListCache<String, DataInput, DataOutput> openTmpStringListCache() throws IOException {
        return openTmpFileListCache(stringToCache);
    }


    public interface ObjectToCache<T, I, O> {

        void saveToCache(O out, T t) throws IOException;

        T loadFromCache(I in) throws IOException;

    }

    public static class StreamInfo<I, O> {

        public I in;

        public O out;

    }

}
