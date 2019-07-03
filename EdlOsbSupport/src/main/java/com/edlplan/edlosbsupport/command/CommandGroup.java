package com.edlplan.edlosbsupport.command;

import android.os.Build;

import com.edlplan.framework.easing.Easing;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

public class CommandGroup implements Serializable {

    public static final Comparator<ACommand> COMPARATOR = (a, b) -> {
        if (a.startTime != b.startTime) {
            return a.startTime < b.startTime ? -1 : 1;
        } else {
            SpriteCommand ac = (SpriteCommand) a;
            SpriteCommand bc = (SpriteCommand) b;
            return Double.compare(ac.endTime, bc.endTime);
        }
    };

    public LinkedList<ACommand> commands = new LinkedList<>();

    private transient double cStartTime = Double.MAX_VALUE;
    public double startTime() {
        if (cStartTime != Double.MAX_VALUE) {
            return cStartTime;
        }
        cStartTime = 1000000000;
        for (ACommand command : commands) {
            if (cStartTime > command.startTime) {
                cStartTime = command.startTime;
            }
        }
        return cStartTime;
    }

    private transient double cEndTime = Double.MIN_VALUE;
    public double endTime() {
        if (cEndTime != Double.MIN_VALUE) {
            return cEndTime;
        }
        cEndTime = -1000000000;
        for (ACommand command : commands) {
            double endTime = command instanceof SpriteCommand ? ((SpriteCommand) command).endTime : ((CommandLoop) command).endTime();
            if (cEndTime < endTime) {
                cEndTime = endTime;
            }
        }
        return cEndTime;
    }

    public double length() {
        return endTime() - startTime();
    }

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
        cStartTime = Double.MAX_VALUE;
        cEndTime = Double.MIN_VALUE;
    }

    @SuppressWarnings("unchecked")
    public void sort() {
        Object[] a = commands.toArray();
        Arrays.sort(a, (Comparator) COMPARATOR);
        ListIterator<ACommand> i = commands.listIterator();
        for (Object e : a) {
            i.next();
            i.set((ACommand) e);
        }
    }

    public void toFlatGroup() {
        ListIterator<ACommand> iterator = commands.listIterator();
        while (iterator.hasNext()) {
            ACommand command = iterator.next();
            if (command instanceof CommandLoop) {
                iterator.remove();
                toFlatGroup(iterator, (CommandLoop) command);
            }
        }
    }

    private void toFlatGroup(ListIterator<ACommand> iterator, CommandLoop loop) {
        loop.innerGroup.toFlatGroup();
        double loopGroupStart = loop.innerGroup.startTime();
        double loopGroupEnd = loop.innerGroup.endTime();
        double loopStartTime = loop.startTime;
        double loopLength = loopGroupEnd - loopGroupStart;
        int loopCount = loop.loopCount;
        if (loopLength < 0) {
            //不合法的loop，省略掉
            return;
        }
        for (int i = 0; i < loopCount; i++, loopStartTime += loopLength) {
            for (ACommand command : loop.innerGroup.commands) {
                iterator.add(((SpriteCommand) command).createOffsetCommand(loopStartTime));
            }
        }
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
