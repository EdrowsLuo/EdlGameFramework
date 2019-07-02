package com.edlplan.edlosbsupport.command;

import java.io.Serializable;

public class CommandFloat extends SpriteCommand implements Serializable {

    public float startValue;

    public float endValue;

    @Override
    public String toString() {
        return super.toString() + "," + startValue + "," + endValue;
    }

}
