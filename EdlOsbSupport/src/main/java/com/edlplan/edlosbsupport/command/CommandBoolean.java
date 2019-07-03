package com.edlplan.edlosbsupport.command;

import java.io.Serializable;

public class CommandBoolean extends SpriteCommand implements Serializable {

    public boolean startValue;

    public boolean endValue;

    @Override
    public SpriteCommand createOffsetCommand(double offset) {
        CommandBoolean command = new CommandBoolean();
        command.startValue = startValue;
        command.endValue = endValue;
        command.easing = easing;
        command.target = target;
        command.startTime = startTime + offset;
        command.endTime = endTime + offset;
        return command;
    }

}
