package com.edlplan.framework.utils.functionality;

import android.view.KeyEvent;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TmpTest {

    public static void main(String[] args) {
        //KeyEvent.KEYCODE_Z;54
        //KeyEvent.KEYCODE_X;52
        System.out.println(0x1b);
        System.out.println(0x1d);
        System.out.println(0x29);
        System.out.println(0x41);
        System.out.println(0x35);
        System.out.println(0x45);
        byte[] a = SmartIterator.wrap(Arrays.asList("01 02 04 07\n09 02 ff 00").iterator())
                .mixElements(value -> SmartIterator.ofArray(value.split("\n")))
                .mixElements(value -> SmartIterator.ofArray(value.split(" ")))
                .applyFunction(value -> Integer.parseInt(value, 16))
                .applyFilter(value -> value == 2)
                .collectAllAsByteArray();
        System.out.println(Arrays.toString(a));
        List<Byte> bytes = SmartIterator.ofArray(a.length, idx -> a[idx]).collectAllAsList();
        for (Iterator<Byte> it = bytes.iterator(); it.hasNext(); ) {
            Byte b = it.next();


        }
    }
}
