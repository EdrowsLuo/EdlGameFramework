package com.edlplan.framework.math.shape;

import com.edlplan.framework.math.Vec2;

public interface IPath {

    int size();

    Vec2[] buffer();

    default int offset() {
        return 0;
    }
}
