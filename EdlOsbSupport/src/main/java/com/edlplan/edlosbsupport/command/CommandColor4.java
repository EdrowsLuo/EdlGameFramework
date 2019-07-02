package com.edlplan.edlosbsupport.command;

import com.edlplan.framework.math.Color4;

import java.io.Serializable;

public class CommandColor4 extends SpriteCommand implements Serializable {

    public Color4 start = Color4.White.copyNew();

    public Color4 end = Color4.White.copyNew();

}
