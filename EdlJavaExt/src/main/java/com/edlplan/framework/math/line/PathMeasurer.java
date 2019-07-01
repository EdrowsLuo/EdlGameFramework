package com.edlplan.framework.math.line;

import com.edlplan.framework.math.Vec2;
import com.edlplan.framework.utils.FloatRef;
import com.edlplan.framework.utils.StructArray;

import java.util.ArrayList;
import java.util.List;

public class PathMeasurer {
    private LinePath path;

    private StructArray<FloatRef> lengthes;

    private StructArray<Vec2> directions;

    private Vec2 endDirection;

    private Vec2 endPoint;

    public PathMeasurer(LinePath path) {
        this.path = path;
    }

    public void onAddPoint(Vec2 v, Vec2 pre) {
        lengthes.add().value = lengthes.get(lengthes.size() - 1).value + Vec2.length(v, pre);
        endDirection.add(v.copy().minus(pre).toNormal());
        measureEndNormal();
    }

    public void clear() {
        lengthes.clear();
        directions.clear();
    }

    private void measureLengthes() {
        if (lengthes == null) {
            lengthes = new StructArray<>(path.size(), FloatRef::new);
        } else {
            lengthes.clear();
        }
        float l = 0;
        StructArray<Vec2> list = path.getAll();
        lengthes.add().value = l;
        Vec2 pre = list.get(0);
        Vec2 cur;
        for (int i = 1; i < list.size(); i++) {
            cur = list.get(i);
            l += Vec2.length(pre, cur);
            lengthes.add().value = l;
            pre = cur;
        }
    }

    private void measureDirections() {
        if (directions == null) {
            directions = new StructArray<>(path.size(), Vec2::new);
        }
        directions.clear();
        if (path.size() < 2) {
            directions.add().set(1, 0);
        } else {
            Vec2 cur;
            Vec2 pre = path.get(0);
            int m = path.size() - 1;
            for (int i = 0; i < m; i++) {
                cur = path.get(i + 1);
                Vec2 vec2 = directions.add();
                vec2.set(cur);
                vec2.minus(pre).toNormal();
                pre = cur;
            }
            directions.add(directions.get(path.size() - 2).copy());
        }
    }

    private void measureEndNormal() {
        endPoint = path.getLast();
        if (path.size() >= 2) {
            endDirection = path.getLast().copy().minus(path.get(path.size() - 2)).toNormal();
        } else {
            endDirection = new Vec2(0, 0);
        }
    }

    public void measure() {
        measureLengthes();
        measureDirections();
        measureEndNormal();
    }

    public float maxLength() {
        return lengthes.get(lengthes.size() - 1).value;
    }

    /**
     *
     */
    public Vec2 atLength(float l) {
        if (l >= maxLength()) {
            return endPoint.copy().add(endDirection.copy().zoom(l - maxLength()));
        } else {
            int s = binarySearch(l);
            float ls = lengthes.get(s).value;
            Vec2 v = Vec2.onLineLength(path.get(s), path.get(s + 1), l - ls);
            return v;
        }
    }

    public Vec2 getTangentLine(float length) {
        return directions.get(Math.max(0, binarySearch(length) - 1));
    }

    //l>=0
    public int binarySearch(float l) {
        if (l >= maxLength()) {
            return lengthes.size() - 1;
        } else {
            return binarySearch(l, 0, lengthes.size() - 1);
        }
    }

    //l>=0&&l<maxLength
    public int binarySearch(float l, int start, int end) {
        if (end - start <= 1) {
            return start;
        } else {
            int m = (start + end) / 2;
            if (lengthes.get(m).value <= l) {
                return binarySearch(l, m, end);
            } else {
                return binarySearch(l, start, m);
            }
        }
    }
}

