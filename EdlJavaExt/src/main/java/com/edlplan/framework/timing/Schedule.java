package com.edlplan.framework.timing;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * 管理一批事件，被动更新时间，在特定时间触发特定事件
 */

public class Schedule implements TimeUpdateable {

    private static Comparator<Task> COMPARATOR = (a, b) -> Double.compare(a.time, b.time);

    private LinkedList<Task> tasks = new LinkedList<>();

    @SuppressWarnings("unchecked")
    public void setTasks(List<Task> tasks) {
        Object[] a = tasks.toArray();
        Arrays.sort(a, (Comparator) COMPARATOR);
        this.tasks.clear();
        for (Object b : a) {
            this.tasks.add((Task) b);
        }
    }

    public LinkedList<Task> getTasks() {
        return tasks;
    }

    /**
     * 添加一个事件
     *
     * @param time     事件事件戳
     * @param runnable 具体操作
     */
    public void addEvent(double time, Runnable runnable) {
        if (tasks.isEmpty()) {
            tasks.add(new Task(time, runnable));
        } else {
            if (time >= tasks.getLast().time) {
                tasks.addLast(new Task(time, runnable));
            } else if (time < tasks.getFirst().time) {
                tasks.addFirst(new Task(time, runnable));
            } else {
                Iterator<Task> iterator = tasks.descendingIterator();
                int idx = tasks.size();
                while (iterator.hasNext()) {
                    final Task t = iterator.next();
                    idx--;
                    if (t.time <= time) {
                        tasks.add(idx + 1, new Task(time, runnable));
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void update(double time) {
        //System.out.println("Update to " + time);
        //if (!tasks.isEmpty()) System.out.println("First " + tasks.getFirst());
        Task task;
        while ((!tasks.isEmpty()) && (task = tasks.getFirst()).time <= time) {
            task.runnable.run();
            tasks.removeFirst();
            //System.out.println("Run " + task);
        }
    }

    public void clear() {
        tasks.clear();
    }

    public static class Task {

        public final double time;

        public final Runnable runnable;

        public Task(double time, Runnable runnable) {
            this.time = time;
            this.runnable = runnable;
        }

        @Override
        public String toString() {
            return "[" + time + "] " + runnable;
        }

    }
}
