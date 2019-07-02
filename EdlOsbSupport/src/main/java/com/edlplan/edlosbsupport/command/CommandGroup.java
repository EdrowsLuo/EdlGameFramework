package com.edlplan.edlosbsupport.command;

import com.edlplan.framework.easing.Easing;

import java.io.Serializable;
import java.util.LinkedList;

public class CommandGroup implements Serializable {

    public LinkedList<ACommand> commands = new LinkedList<>();

    public void addCommand(
            Target target,
            double startTime,
            double endTime,
            float startValue,
            float endValue,
            Easing easing) {
        CommandFloat commandFloat = new CommandFloat();
        commandFloat.startTime = startTime;
        commandFloat.endTime = endTime;
        commandFloat.easing = easing;
        commandFloat.target = target;
        commandFloat.startValue = startValue;
        commandFloat.endValue = endValue;
        commands.add(commandFloat);
    }

    public void addCommand(
            Target target,
            double startTime,
            double endTime,
            boolean startValue,
            boolean endValue,
            Easing easing) {
        CommandBoolean commandBoolean = new CommandBoolean();
        commandBoolean.startTime = startTime;
        commandBoolean.endTime = endTime;
        commandBoolean.easing = easing;
        commandBoolean.target = target;
        commandBoolean.startValue = startValue;
        commandBoolean.endValue = endValue;
        commands.add(commandBoolean);
    }

    public CommandColor4 addColorCommand(
            Target target,
            double startTime,
            double endTime,
            Easing easing) {
        CommandColor4 command = new CommandColor4();
        command.startTime = startTime;
        command.endTime = endTime;
        command.easing = easing;
        command.target = target;
        commands.add(command);
        return command;
    }

    public void clear() {
        for (ACommand command : commands) {
            if (command instanceof CommandLoop) {
                ((CommandLoop) command).innerGroup.clear();
            }
        }
        commands.clear();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ACommand command : commands) {
            stringBuilder.append("\n").append(command);
        }
        return stringBuilder.toString();
    }
}
