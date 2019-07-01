package com.edlplan.framework.math.line;

import com.edlplan.framework.math.Vec2;

import java.util.ArrayList;
import java.util.List;

public interface AbstractPath {

    Vec2 get(int idx);

    int size();

    default LinePath fitToLinePath() {
        LinePath list = new LinePath();
        int size = size();
        Vec2 pre = null;
        for (int i = 0; i < size; i++) {
            Vec2 cur = new Vec2(get(i));
            if (pre == null || Vec2.length(pre, cur) > 0.01) {
                list.add(pre = cur);
            }
        }
        return list;
    }

    default LinePath fitToLinePath(LinePath cache) {
        cache.clear();
        int size = size();
        Vec2 pre = null;
        for (int i = 0; i < size; i++) {
            Vec2 cur = new Vec2(get(i));
            if (pre == null || Vec2.length(pre, cur) > 0.01) {
                cache.add(pre = cur);
            }
        }
        return cache;
    }
}
