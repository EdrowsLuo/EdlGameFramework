package com.edlplan.edlosbsupport.command;

import java.io.Serializable;

public class CommandFloat extends SpriteCommand implements Serializable {

    public float startValue;

    public float endValue;

    @Override
    public SpriteCommand createOffsetCommand(double offset) {
        CommandFloat command = new CommandFloat();
        command.startValue = startValue;
        command.endValue = endValue;
        command.easing = easing;
        command.target = target;
        command.startTime = startTime + offset;
        command.endTime = endTime + offset;
        return command;
    }

    @Override
    public String toString() {
        return super.toString() + "," + startValue + "," + endValue;
    }

}
