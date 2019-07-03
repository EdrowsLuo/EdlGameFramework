package com.edlplan.edlosbsupport.player;

import com.edlplan.edlosbsupport.command.SpriteCommand;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandHandleTimeline<T extends SpriteCommand> {

    protected T currentCommand;

    private int currentIndex = -1;

    private List<T> commandList = new ArrayList<>();

    /**
     * 请确保是排序后再被添加进来的
     */
    public void addCommand(T command) {
        commandList.add(command);
    }

    private void findCurrentCommand(double time) {
        T c = null;
        int idx = 0;
        while (idx < commandList.size()) {
            c = commandList.get(idx);
            if (c.endTime >= time) {
                break;
            }
            idx++;
        }
        if (c.endTime < time || c.startTime <= time) {
            currentIndex = idx;
            currentCommand = c;
        } else {
            idx--;
            currentIndex = idx;
            currentCommand = commandList.get(idx);
        }
        //System.out.println("found " + idx + " " + commandList.get(idx));
    }

    public void update(double time) {
        final int commandListSize = commandList.size();
        if (commandListSize == 0) {
            return;
        }

        if (currentCommand == null) {
            currentCommand = commandList.get(0);
        }


        /**
         * 已经超出了当前指令的控制范围，寻找下一个指令
         */
        if (currentIndex < commandListSize - 1 && currentCommand.endTime < time) {
            int idx = currentIndex + 1;
            while (idx < commandListSize && commandList.get(idx).endTime < time) {
                idx++;
            }
            if (idx != commandListSize) {
                final T n = commandList.get(idx);
                if (n.startTime < time) {
                    currentCommand = n;
                } else {
                    idx--;
                    currentCommand = commandList.get(idx);
                }
                currentIndex = idx;
            } else {
                currentIndex = commandListSize - 1;
                currentCommand = commandList.get(currentIndex);
            }
        }

        applyValue(time);

    }

    protected abstract void applyValue(double time);

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getClass());
        for (T t : commandList) {
            stringBuilder.append('\n').append(t);
        }
        return stringBuilder.toString();
    }
}
