package com.edlplan.edlosbsupport.player;

import com.edlplan.edlosbsupport.command.CommandColor4;
import com.edlplan.framework.math.Color4;

public class CommandColor4HandleTimeline extends CommandHandleTimeline<CommandColor4> {

    public Color4 value;

    @Override
    protected void applyValue(double time) {
        if (currentCommand.endTime < time) {
            value.set(currentCommand.endValue);
        } else {
            value.set(currentCommand.startValue, currentCommand.endValue, currentCommand.getProgress(time));
        }
    }
}
