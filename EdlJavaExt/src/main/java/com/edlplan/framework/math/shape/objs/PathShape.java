package com.edlplan.framework.math.shape.objs;

import com.edlplan.framework.math.shape.IHasPath;
import com.edlplan.framework.math.shape.Shape;

public interface PathShape extends Shape, IHasPath {

    default boolean isComplexShape() {
        return false;
    }
}
