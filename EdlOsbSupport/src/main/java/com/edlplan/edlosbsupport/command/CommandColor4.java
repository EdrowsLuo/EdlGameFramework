package com.edlplan.edlosbsupport.command;

import com.edlplan.framework.math.Color4;

import java.io.Serializable;

public class CommandColor4 extends SpriteCommand implements Serializable {

    public Color4 startValue = Color4.White.copyNew();

    public Color4 endValue = Color4.White.copyNew();

    @Override
    public SpriteCommand createOffsetCommand(double offset) {
        CommandColor4 command = new CommandColor4();
        command.startValue = startValue;
        command.endValue = endValue;
        command.easing = easing;
        command.target = target;
        command.startTime = startTime + offset;
        command.endTime = endTime + offset;
        return command;
    }

}
