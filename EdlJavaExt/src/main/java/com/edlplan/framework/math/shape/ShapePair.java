package com.edlplan.framework.math.shape;

public class ShapePair implements Shape {

    public Shape dst;

    public Shape src;

    @Override
    public boolean isComplexShape() {
        return true;
    }
}
