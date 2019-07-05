package com.edlplan.framework.utils;

import java.util.Iterator;

public class CharArray {

    public static final Cache<CharArray> cache = new Cache<>(CharArray::new, CharArray::clear, 100);

    public static void saveAllUnusedObject(CharArray... toCache) {
        for (int i = 0; i < toCache.length; i++) {
            if (cache.isFull()) {
                break;
            }
            cache.save(toCache[i]);
        }
    }

    public static void saveUnusedObject(CharArray toCache) {
        cache.save(toCache);
    }

    public static CharArray getCachedObject() {
        return cache.get();
    }

    public char[] ary;
    public int offset;
    public int end;

    public CharArray(char[] ary, int offset, int end) {
        this.ary = ary;
        this.offset = offset;
        this.end = end == -1 ? ary.length : end;
    }

    public CharArray(CharArray copy) {
        this.ary = copy.ary;
        this.offset = copy.offset;
        this.end = copy.end;
    }

    public CharArray(String s) {
        this(s.toCharArray(), 0, -1);
    }

    private CharArray() {

    }

    private static void clear(CharArray charArray) {
        charArray.ary = null;
        charArray.offset = 0;
        charArray.end = 0;
    }

    public void set(String s, int offset, int l) {
        ary = s.toCharArray();
        if (l==-1) l = ary.length;
        this.offset = offset;
        end = offset + l;
    }

    public int length() {
        return end - offset;
    }

    public char get(int i) {
        return ary[offset + i];
    }

    public boolean contains(char c) {
        for (int i = offset; i < end; i++) {
            if (ary[i] == c) {
                return true;
            }
        }
        return false;
    }

    private static int[] powers = new int[]{
            0,
            1,
            10,
            100,
            1000,
            10000,
            100000,
            1000000,
            10000000,
            100000000,
            1000000000,
    };
    private int parsePlainInt() {
        if (empty()) return 0;
        int co = offset;
        int sign;
        switch (ary[offset]) {
            case '-':
                sign = -1;
                co++;
                break;
            case '+':
                sign = 1;
                co++;
                break;
            default:
                sign = 1;
        }
        int p = 0;
        for (int i = end - 1; i >= co; i--) {
            p += (ary[i] - '0') * powers[end - i];
        }
        return p * sign;
    }

    public int parseSimpleInt() throws NumberFormatException{
        if (contains('.')) throw new NumberFormatException("error parse " + this + " to int");
        return parsePlainInt();
    }

    public float parseSimpleFloat() throws NumberFormatException{
        if (contains('.')) return Float.parseFloat(this.toString());
        return parsePlainInt();
    }

    public double parseSimpleDouble() throws NumberFormatException {
        if (contains('.')) return Double.parseDouble(this.toString());
        return parsePlainInt();
    }


    public void trimBegin() {
        while (offset < end && (ary[offset] == ' ' || ary[offset] == '\n' || ary[offset] == 'r')) {
            offset++;
        }
    }

    public boolean empty() {
        return offset >= end;
    }

    public void nextChar(char target) {
        if (empty() || get(0) != target) {
            throw new FormatNotMatchException(target + " not found at " + offset);
        }
        offset++;
    }

    public CharArraySplitIterator split(char code) {
        return new CharArraySplitIterator(code);
    }

    @Override
    public String toString() {
        return new String(ary, offset, end - offset);
    }

    public static class FormatNotMatchException extends RuntimeException {
        public FormatNotMatchException(String msg) {
            super(msg);
        }
    }


    public class CharArraySplitIterator implements Iterator<CharArray> {

        int splPos1, splPos2;
        char code;

        private boolean autoTrim = false;

        private boolean autoCache = false;

        private CharArray pr;

        public CharArraySplitIterator(char code) {
            this.code = code;
            splPos1 = offset;
            splPos2 = offset;
            while (splPos2 < end && ary[splPos2] != code) {
                splPos2++;
            }
        }

        /**
         * 当开启自动缓存时，上一次调用next产生的ary会在下一次被清空
         */
        public void setAutoCache(boolean autoCache) {
            if (this.autoCache != autoCache) {
                this.autoCache = autoCache;
                if (autoCache) {
                    pr = null;
                } else {
                    if (pr != null) {
                        saveUnusedObject(pr);
                        pr = null;
                    }
                }
            }
        }

        public void setAutoTrim(boolean autoTrim) {
            this.autoTrim = autoTrim;
        }

        @Override
        public boolean hasNext() {
            return splPos1 < end;
        }

        @Override
        public CharArray next() {
            if (autoCache && pr != null) {
                saveUnusedObject(pr);
                pr = null;
            }
            if (!hasNext())
                throw new IndexOutOfBoundsException("split result is end! " + CharArray.this.toString());
            CharArray array = cache.get();
            array.ary = ary;
            array.offset = splPos1;
            array.end = splPos2;
            splPos2++;
            splPos1 = splPos2;
            while (splPos2 < end && ary[splPos2] != code) {
                splPos2++;
            }
            if (autoTrim) array.trimBegin();
            if (autoCache) pr = array;
            return array;
        }

        public void close() {
            if (autoCache && pr != null) {
                saveUnusedObject(pr);
                pr = null;
            }
        }

    }

}