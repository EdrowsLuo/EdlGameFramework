package com.edlplan.edlosbsupport.player;

import com.edlplan.edlosbsupport.command.CommandFloat;
import com.edlplan.framework.utils.FloatRef;

public class CommandFloatHandleTimeline extends CommandHandleTimeline<CommandFloat> {

    public FloatRef value;

    public CommandFloatHandleTimeline(FloatRef value) {
        this.value = value;
    }

    @Override
    protected void applyValue(double time) {
        //System.out.println("Apply Value " + currentCommand + " " + time);
        if (currentCommand.endTime < time) {
            value.value = currentCommand.endValue;
        } else {
            float es = currentCommand.getProgress(time);
            value.value = currentCommand.startValue * (1 - es) + currentCommand.endValue * es;
        }
    }
}
