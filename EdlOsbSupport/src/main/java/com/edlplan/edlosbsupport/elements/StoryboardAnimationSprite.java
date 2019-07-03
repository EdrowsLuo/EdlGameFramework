package com.edlplan.edlosbsupport.elements;

import com.edlplan.framework.utils.CharArray;

import java.io.Serializable;
import java.util.Set;

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

    public String buildPath(int index) {
        return spriteFilename.substring(
                0,
                spriteFilename.lastIndexOf("."))
                + index
                + spriteFilename.substring(spriteFilename.lastIndexOf("."), spriteFilename.length());
    }

    @Override
    public void addAllTextures(Set<String> textures) {
        for (int i = 0; i < frameCount; i++) {
            textures.add(buildPath(i));
        }
    }
}
