package com.edlplan.edlosbsupport.elements;

import com.edlplan.framework.utils.CharArray;

public enum ElementType {
    Background(0),
    Video(1),
    Break(2),
    Colour(3),
    Sprite(4),
    Sample(5),
    Animation(6);
    private final int value;

    ElementType(int v) {
        value = v;
    }

    public static ElementType parse(CharArray s) {
        if (s.length() == 0) return null;
        char f = s.get(0);
        if (f >= '0' && f <= '6') {
            return values()[f - '0'];
        } else {
            switch (f) {
                case 'S':
                    char x = s.get(1);
                    return x == 'p' ? Sprite : Sample;
                case 'A':
                    return Animation;
                case 'B':
                    char c = s.get(1);
                    return c == 'a' ? Background : Break;
                case 'V':
                    return Video;
                case 'C':
                    return Colour;
                default:
                    return null;
            }
        }
    }

}
