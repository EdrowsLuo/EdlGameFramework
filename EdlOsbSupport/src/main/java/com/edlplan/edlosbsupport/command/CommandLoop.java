package com.edlplan.edlosbsupport.command;

import java.io.Serializable;

public class CommandLoop extends ACommand implements Serializable {

    public CommandGroup innerGroup = new CommandGroup();

    public double startTime;

    public int loopCount;

}
