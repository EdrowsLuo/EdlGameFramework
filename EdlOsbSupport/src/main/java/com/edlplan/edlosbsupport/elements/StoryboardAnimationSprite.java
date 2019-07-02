package com.edlplan.edlosbsupport.elements;

import com.edlplan.framework.utils.CharArray;

import java.io.Serializable;

public class StoryboardAnimationSprite extends StoryboardSprite implements Serializable {

    public enum LoopType {
        LoopOnce,
        LoopForever;

        public static LoopType parse(CharArray charArray) {
            switch (charArray.length()) {
                case 1:
                    return charArray.get(0) == '0' ? LoopOnce : LoopForever;
                case 8:
                    return LoopOnce;
                case 11:
                    return LoopForever;
                default:
                    return null;
            }
        }
    }

    public LoopType loopType;

    public int frameCount;

    public double frameDelay;

}
