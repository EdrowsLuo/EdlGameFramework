package com.edlplan.edlosbsupport.command;

import com.edlplan.framework.easing.Easing;
import com.edlplan.framework.easing.EasingManager;

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

    public abstract SpriteCommand createOffsetCommand(double offset);

    public float getProgress(double time) {
        if (startTime == endTime) {
            return time < startTime ? 0 : 1;
        } else if (time < startTime) {
            return 0;
        } else if (time > endTime) {
            return 1;
        } else {
            return (float) EasingManager.apply(easing, (time - startTime) / (endTime - startTime));
        }
    }

    @Override
    public String toString() {
        return target + "," + easing + "," + startTime + "," + endTime;
    }
}
