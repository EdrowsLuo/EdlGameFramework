package com.edlplan.edlosbsupport.command;

import com.edlplan.framework.easing.Easing;

import java.io.Serializable;

public abstract class SpriteCommand extends ACommand implements Serializable {

    public enum TargetType{
        FLOAT,
        BOOLEAN,
        COLOR4
    }

    public double endTime;

    public Easing easing;

    public Target target;

    @Override
    public String toString() {
        return target + "," + easing + "," + startTime + "," + endTime;
    }
}
