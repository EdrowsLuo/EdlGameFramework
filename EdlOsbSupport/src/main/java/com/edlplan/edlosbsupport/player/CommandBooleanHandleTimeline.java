package com.edlplan.edlosbsupport.player;

import com.edlplan.edlosbsupport.command.CommandBoolean;
import com.edlplan.framework.utils.BooleanRef;

public class CommandBooleanHandleTimeline extends CommandHandleTimeline<CommandBoolean> {

    public BooleanRef value;

    @Override
    protected void applyValue(double time) {
        if (currentCommand.endTime < time) {
            value.value = currentCommand.endValue;
        } else {
            value.value = currentCommand.startValue;
        }
    }
}
