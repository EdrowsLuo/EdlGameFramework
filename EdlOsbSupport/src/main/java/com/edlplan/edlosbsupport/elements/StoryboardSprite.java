package com.edlplan.edlosbsupport.elements;

import com.edlplan.edlosbsupport.command.CommandGroup;
import com.edlplan.framework.math.Anchor;
import com.edlplan.framework.utils.CharArray;

import java.io.Serializable;
import java.util.Set;

public class StoryboardSprite extends IStoryboardElement implements Serializable{

    public enum Layer {
        Background(3, true, true),
        Fail(2, false, true),
        Pass(1, true, false),
        Foreground(0, true, true);
        private final int depth;
        private final boolean enableWhenPassing;
        private final boolean enableWhenFailing;

        Layer(int depth, boolean ewp, boolean ewf) {
            this.depth = depth;
            this.enableWhenPassing = ewp;
            this.enableWhenFailing = ewf;
        }

        public static Layer parse(CharArray s) {
            if (s.length() == 0) return null;
            char f = s.get(0);
            if (f >= '0' && f <= '3') {
                return Layer.values()[f - '0'];
            } else {
                switch (f) {
                    case 'B':
                        return Background;
                    case 'P':
                        return Pass;
                    case 'F':
                        char c = s.get(1);
                        return c == 'a' ? Fail : Foreground;
                    default:
                        return null;
                }
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < Origin.values().length; i++) {
            Origin origin = Origin.values()[i];
            String name = origin.name();
            System.out.println(name + " : " + Origin.parse(new CharArray(name)));
        }
    }

    public enum Origin {
        TopLeft(Anchor.TopLeft),
        Centre(Anchor.Center),
        CentreLeft(Anchor.CenterLeft),
        TopRight(Anchor.TopRight),
        BottomCentre(Anchor.BottomCenter),
        TopCentre(Anchor.TopCenter),
        CentreRight(Anchor.CenterRight),
        BottomLeft(Anchor.BottomLeft),
        BottomRight(Anchor.BottomRight);
        public final Anchor value;

        Origin(Anchor anchor) {
            value = anchor;
        }

        public static Origin parse(CharArray charArray) {
            int l = charArray.length();
            switch (l) {
                case 6:
                    return Centre;
                case 1:
                    int i = charArray.get(0) - '0';
                    if (i >= 0 && i < 9) {
                        return values()[i];
                    } else {
                        return null;
                    }
                case 7:
                    return TopLeft;
                case 8:
                    return TopRight;
                case 9:
                    return TopCentre;
                case 12:
                    return BottomCentre;
                case 10:
                    return charArray.get(0) == 'C' ? CentreLeft : BottomLeft;
                case 11:
                    return charArray.get(0) == 'C' ? CentreRight : BottomRight;
            }
            return null;
        }

    }

    public CommandGroup rootGroup = new CommandGroup();

    public Layer layer;

    public Origin origin;

    public String spriteFilename;

    public float startX, startY;

    public transient int depth;

    public void toFlat() {
        rootGroup.toFlatGroup();
    }

    /**
     * 只能在toFlat之后调用
     */
    public void sort() {
        rootGroup.sort();
    }

    public double startTime() {
        return rootGroup.startTime();
    }

    public double endTime() {
        return rootGroup.endTime();
    }

    public void addAllTextures(Set<String> textures) {
        textures.add(spriteFilename);
    }

    @Override
    public void clear() {
        rootGroup.clear();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(layer).append(",")
                .append(spriteFilename).append(",")
                .append(origin).append(",")
                .append(startX).append(",")
                .append(startY).append('\n')
                .append(rootGroup);
        return stringBuilder.toString();
    }
}
