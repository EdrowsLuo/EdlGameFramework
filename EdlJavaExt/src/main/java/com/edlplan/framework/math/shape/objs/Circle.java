package com.edlplan.framework.math.shape.objs;

import com.edlplan.framework.math.shape.IPath;
import com.edlplan.framework.math.shape.Path;
import com.edlplan.framework.math.shape.PathBuilder;
import com.edlplan.framework.math.FMath;
import com.edlplan.framework.math.Vec2;

public class Circle implements PathShape {

    private Vec2 origin = new Vec2();

    private float radius;

    public Circle() {

    }

    public Circle(Vec2 origin, float radius) {
        this.origin.set(origin);
        this.radius = radius;
    }

    public void setOrigin(Vec2 origin) {
        this.origin.set(origin);
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public IPath createPath() {
        Path path = new Path(48);
        PathBuilder builder = new PathBuilder(path);
        builder.moveTo(origin.x + radius, origin.y);
        builder.circleRotate(origin, FMath.Pi2);
        return path;
    }

    @Override
    public boolean isComplexShape() {
        return false;
    }
}
