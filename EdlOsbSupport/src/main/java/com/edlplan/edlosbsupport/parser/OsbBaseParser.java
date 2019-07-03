package com.edlplan.edlosbsupport.parser;

import com.edlplan.edlosbsupport.OsuStoryboard;
import com.edlplan.edlosbsupport.command.CommandColor4;
import com.edlplan.edlosbsupport.command.CommandGroup;
import com.edlplan.edlosbsupport.command.CommandLoop;
import com.edlplan.edlosbsupport.command.Target;
import com.edlplan.edlosbsupport.elements.ElementType;
import com.edlplan.edlosbsupport.elements.StoryboardAnimationSprite;
import com.edlplan.edlosbsupport.elements.StoryboardSprite;
import com.edlplan.framework.easing.Easing;
import com.edlplan.framework.math.Color4;
import com.edlplan.framework.utils.CharArray;

import java.util.Stack;

/**
 *  解析一串文本，不提供变量支持，不解析ini tag，不支持注释
 */
public class OsbBaseParser {

    private OsuStoryboard storyboard;

    private StoryboardSprite sprite;

    private Stack<CommandGroup> commandGroupStack = new Stack<>();

    private int savedDepth = -1;

    public OsbBaseParser(OsuStoryboard storyboard) {
        this.storyboard = storyboard;
    }

    public OsuStoryboard getStoryboard() {
        return storyboard;
    }

    public void setStoryboard(OsuStoryboard storyboard) {
        this.storyboard = storyboard;
    }

    public void clear() {
        savedDepth = -1;
        storyboard = null;
        sprite = null;
        commandGroupStack.clear();
    }

    public static int parseDepth(CharArray s) {
        int depth = 0;
        final int l = s.length();
        char c;
        while (depth < l) {
            c = s.get(depth);
            if (c == '_' || c == ' ') {
                depth++;
            } else {
                break;
            }
        }
        s.offset += depth;
        return depth;
    }

    private String cleanFilename(String s) throws OsbParseException {
        if (s.length() >= 2 && s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
            return s.substring(1, s.length() - 1).replace('\\', '/');
        } else {
            throw new OsbParseException("err filename : " + s);
        }
    }

    public void appendLine(CharArray line) throws OsbParseException {
        try {
            int depth = parseDepth(line);
            CharArray.CharArraySplitIterator spl = line.split(',');
            spl.setAutoTrim(true);
            spl.setAutoCache(true);
            if (depth == 0) {
                savedDepth = 0;
                sprite = null;
                commandGroupStack.clear();
                ElementType eventType = ElementType.parse(spl.next());
                if (eventType == null) {
                    throw new OsbParseException("unknown element type " + line);
                }
                switch (eventType) {
                    case Sprite: {
                        StoryboardSprite.Layer layer = StoryboardSprite.Layer.parse(spl.next());
                        StoryboardSprite.Origin origin = StoryboardSprite.Origin.parse(spl.next());
                        String path = cleanFilename(spl.next().toString());
                        float x = Float.parseFloat(spl.next().toString());
                        float y = Float.parseFloat(spl.next().toString());
                        sprite = new StoryboardSprite();
                        sprite.layer = layer;
                        sprite.origin = origin;
                        sprite.spriteFilename = path;
                        sprite.startX = x;
                        sprite.startY = y;
                        commandGroupStack.push(sprite.rootGroup);
                        storyboard.addElement(sprite);
                    }
                    break;
                    case Animation: {
                        StoryboardSprite.Layer layer = StoryboardSprite.Layer.parse(spl.next());
                        StoryboardSprite.Origin origin = StoryboardSprite.Origin.parse(spl.next());
                        String path = cleanFilename(spl.next().toString());
                        float x = Float.parseFloat(spl.next().toString());
                        float y = Float.parseFloat(spl.next().toString());
                        int frameCount = Integer.parseInt(spl.next().toString());
                        double frameDelay = Double.parseDouble(spl.next().toString());
                        StoryboardAnimationSprite.LoopType loopType = StoryboardAnimationSprite.LoopType.parse(spl.next());
                        StoryboardAnimationSprite sprite = new StoryboardAnimationSprite();
                        sprite.layer = layer;
                        sprite.origin = origin;
                        sprite.spriteFilename = path;
                        sprite.startX = x;
                        sprite.startY = y;
                        sprite.frameCount = frameCount;
                        sprite.frameDelay = frameDelay;
                        sprite.loopType = loopType;

                        this.sprite = sprite;
                        commandGroupStack.push(sprite.rootGroup);
                        storyboard.addElement(sprite);

                    }
                    break;
                    case Sample: {
                        //TODO : Add Storyboard Sample support
                    }
                    break;
                }
            } else {

                while (savedDepth > depth) {
                    savedDepth--;
                    commandGroupStack.pop();
                }
                savedDepth = depth;
                CommandGroup group = commandGroupStack.peek();
                spl.setAutoCache(false);
                CharArray commandType = spl.next();
                spl.setAutoCache(true);
                switch (commandType.get(0)) {
                    case 'T': {
                        //TODO : Add trigger support
                        commandGroupStack.push(new CommandGroup());
                    }
                    break;
                    case 'L': {
                        double startTime = Double.parseDouble(spl.next().toString());
                        int loopCount = Integer.parseInt(spl.next().toString());
                        CommandLoop loop = new CommandLoop();
                        loop.loopCount = loopCount;
                        loop.startTime = startTime;
                        group.commands.add(loop);
                        commandGroupStack.push(loop.innerGroup);
                    }
                    break;
                    default: {

                        Easing easing = Easing.values()[spl.next().get(0) - '0'];
                        double startTime = Double.parseDouble(spl.next().toString());

                        spl.setAutoCache(false);
                        CharArray s3;
                        s3 = spl.hasNext() ? spl.next() : null;
                        spl.setAutoCache(true);

                        double endTime = (s3 == null || s3.length() == 0) ? startTime : Double.parseDouble(s3.toString());

                        CharArray.saveAllUnusedObject(s3);


                        switch (commandType.get(0)) {
                            case 'F': {
                                float startValue = Float.parseFloat(spl.next().toString());
                                float endValue = spl.hasNext() ? Float.parseFloat(spl.next().toString()) : startValue;
                                group.addCommand(Target.Alpha, startTime, endTime, startValue, endValue, easing);
                            }
                            break;
                            case 'S': {
                                float startValue = Float.parseFloat(spl.next().toString());
                                float endValue = spl.hasNext() ? Float.parseFloat(spl.next().toString()) : startValue;
                                group.addCommand(Target.ScaleX, startTime, endTime, startValue, endValue, easing);
                                group.addCommand(Target.ScaleY, startTime, endTime, startValue, endValue, easing);
                            }
                            break;
                            case 'V': {
                                float startX = Float.parseFloat(spl.next().toString());
                                float startY = Float.parseFloat(spl.next().toString());
                                float endX = spl.hasNext() ? Float.parseFloat(spl.next().toString()) : startX;
                                float endY = spl.hasNext() ? Float.parseFloat(spl.next().toString()) : startY;
                                group.addCommand(Target.ScaleX, startTime, endTime, startX, endX, easing);
                                group.addCommand(Target.ScaleY, startTime, endTime, startY, endY, easing);
                            }
                            break;
                            case 'R': {
                                float startValue = Float.parseFloat(spl.next().toString());
                                float endValue = spl.hasNext() ? Float.parseFloat(spl.next().toString()) : startValue;
                                group.addCommand(Target.Rotation, startTime, endTime, startValue, endValue, easing);
                            }
                            break;
                            case 'M': {
                                if (commandType.length() == 1) {
                                    float startX = Float.parseFloat(spl.next().toString());
                                    float startY = Float.parseFloat(spl.next().toString());
                                    float endX = spl.hasNext() ? Float.parseFloat(spl.next().toString()) : startX;
                                    float endY = spl.hasNext() ? Float.parseFloat(spl.next().toString()) : startY;
                                    group.addCommand(Target.X, startTime, endTime, startX, endX, easing);
                                    group.addCommand(Target.Y, startTime, endTime, startY, endY, easing);
                                } else {
                                    switch (commandType.get(1)) {
                                        case 'X': {
                                            float startValue = Float.parseFloat(spl.next().toString());
                                            float endValue = spl.hasNext() ? Float.parseFloat(spl.next().toString()) : startValue;
                                            group.addCommand(Target.X, startTime, endTime, startValue, endValue, easing);
                                        }
                                        break;
                                        case 'Y': {
                                            float startValue = Float.parseFloat(spl.next().toString());
                                            float endValue = spl.hasNext() ? Float.parseFloat(spl.next().toString()) : startValue;
                                            group.addCommand(Target.Y, startTime, endTime, startValue, endValue, easing);
                                        }
                                        break;
                                    }
                                }

                            }
                            break;
                            case 'C': {
                                float startR = Float.parseFloat(spl.next().toString());
                                float startG = Float.parseFloat(spl.next().toString());
                                float startB = Float.parseFloat(spl.next().toString());
                                float endR = spl.hasNext() ? Float.parseFloat(spl.next().toString()) : startR;
                                float endG = spl.hasNext() ? Float.parseFloat(spl.next().toString()) : startG;
                                float endB = spl.hasNext() ? Float.parseFloat(spl.next().toString()) : startB;
                                CommandColor4 commandColor4 = group.addColorCommand(Target.Color, startTime, endTime, easing);
                                Color4.rgb255(startR, startG, startB, commandColor4.startValue);
                                Color4.rgb255(endR, endG, endB, commandColor4.endValue);
                            }
                            break;
                            case 'P': {
                                switch (spl.next().get(0)) {
                                    case 'A':
                                        group.addCommand(Target.BlendingMode, startTime, endTime, true, startTime == endTime, easing);
                                        break;
                                    case 'H':
                                        group.addCommand(Target.FlipH, startTime, endTime, true, startTime == endTime, easing);
                                        break;
                                    case 'V':
                                        group.addCommand(Target.FlipV, startTime, endTime, true, startTime == endTime, easing);
                                        break;
                                }
                            }
                            break;
                            default:
                                throw new OsbParseException("unknow command type: " + commandType);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Osb err at " + line);
            e.printStackTrace();
            throw new OsbParseException(e);
        }

    }


}
