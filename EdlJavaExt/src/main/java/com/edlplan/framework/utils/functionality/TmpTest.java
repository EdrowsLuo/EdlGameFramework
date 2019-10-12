package com.edlplan.framework.utils.functionality;

import java.util.Arrays;

public class TmpTest {

    public static void main(String[] args) {
        byte[] a = SmartIterator.wrap(Arrays.asList("01 02 04 07\n09 02 ff 00").iterator())
                .mixElements(value -> SmartIterator.ofArray(value.split("\n")))
                .mixElements(value -> SmartIterator.ofArray(value.split(" ")))
                .applyFunction(value -> Integer.parseInt(value, 16))
                .collectAllAsByteArray();
        System.out.println(Arrays.toString(a));
    }
}
