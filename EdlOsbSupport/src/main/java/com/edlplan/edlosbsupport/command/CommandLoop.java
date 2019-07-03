package com.edlplan.edlosbsupport.command;

import java.io.Serializable;

public class CommandLoop extends ACommand implements Serializable {

    public CommandGroup innerGroup = new CommandGroup();

    public double startTime;

    public int loopCount;

    private transient double cEndTime = Double.MIN_VALUE;

    public double endTime() {
        if (cEndTime != Double.MIN_VALUE) {
            return cEndTime;
        } else {
            cEndTime = startTime + innerGroup.startTime() + loopCount * innerGroup.length();
            return cEndTime;
        }
    }

}
