package com.edlplan.framework.math.line;

import com.edlplan.framework.math.Vec2;
import com.edlplan.framework.utils.StructArray;

public class LinePath implements AbstractPath {
    private StructArray<Vec2> positions;

    private PathMeasurer measurer, measurerCached;

    public LinePath() {
        positions = new StructArray<>(Vec2::new);
    }

    public PathMeasurer getMeasurer() {
        return measurer;
    }

    public void measure() {
        if (measurer == null) {
            measurer = measurerCached == null ? new PathMeasurer(this) : measurerCached;
        }
        measurer.measure();
    }

    public void clear() {
        positions.clear();
        measurerCached = measurer;
        measurer = null;
    }

    /**
     * 扩展到指定长度
     */
    public void bufferLength(float length) {
        if (measurer.maxLength() >= length) return;
        Vec2 added = measurer.atLength(length);
        if (Vec2.length(getLast(), added) > 1) {
            add(added);
            measurer.onAddPoint(added, positions.get(positions.size() - 2));
        }
    }

    @Override
    public int size() {
        return positions.size();
    }

    @Override
    public Vec2 get(int index) {
        return positions.get(index);
    }

    public Vec2 getLast() {
        return get(size() - 1);
    }

    public StructArray<Vec2> getAll() {
        return positions;
    }

    public void add(Vec2 v) {
        positions.add(v);
    }

    public AbstractPath cutPath(float start, float end) {

        int s = measurer.binarySearch(start) + 1;
        int e = measurer.binarySearch(end);
        Vec2 startPoint = measurer.atLength(start);
        Vec2 endPoint = measurer.atLength(end);

        /**
         *这部分是用来防止当剪切路径刚好在路径点上的情况。。
         *不过现在测试感觉没什么用，暂时注释掉
         */

		/*
		if(Vec2.near(get(s),startPoint,tolerance)){
			if(Vec2.near(get(e),endPoint,tolerance)){
				return new SubLinePath(s,e);
			}else{
				return new SubLinePathR(endPoint,s,e);
			}
		}else{
			if(Vec2.near(get(e),endPoint,tolerance)){
				return new SubLinePathL(startPoint,s,e);
			}else{
				return new SubLinePathLR(startPoint,endPoint,s,e);
			}
		}*/

        return new SubLinePathLR(startPoint, endPoint, s, e).fitToLinePath();
    }

    public class SubLinePath implements AbstractPath {

        private int startIndex;

        private int size;

        public SubLinePath(int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.size = endIndex - startIndex + 1;
        }

        @Override
        public Vec2 get(int idx) {

            return LinePath.this.get(startIndex + idx);
        }

        @Override
        public int size() {

            return size;
        }

    }

    public class SubLinePathL implements AbstractPath {

        private Vec2 startPoint;

        private int startIndex;

        private int startIndexM1;

        private int size;

        public SubLinePathL(Vec2 startPoint, int startIndex, int endIndex) {
            this.startPoint = startPoint;
            this.startIndex = startIndex;
            startIndexM1 = startIndex - 1;
            this.size = endIndex - startIndex + 2;
        }

        @Override
        public Vec2 get(int idx) {

            return (idx == 0) ? startPoint : LinePath.this.get(startIndexM1 + idx);
        }

        @Override
        public int size() {

            return size;
        }

    }

    public class SubLinePathR implements AbstractPath {

        private Vec2 endPoint;

        private int startIndex;

        private int endIndexA1;

        private int size;

        public SubLinePathR(Vec2 endPoint, int startIndex, int endIndex) {
            this.endPoint = endPoint;
            this.startIndex = startIndex;
            this.endIndexA1 = endIndex + 1;
            this.size = endIndex - startIndex + 2;
        }

        @Override
        public Vec2 get(int idx) {

            return (idx == endIndexA1) ? endPoint : LinePath.this.get(startIndex + idx);
        }

        @Override
        public int size() {

            return size;
        }

    }

    public class SubLinePathLR implements AbstractPath {

        private Vec2 startPoint;

        private Vec2 endPoint;

        private int endPointIdx;

        private int startIndexM1;

        private int size;

        public SubLinePathLR(Vec2 startPoint, Vec2 endPoint, int startIndex, int endIndex) {
            this.endPoint = endPoint;
            this.startPoint = startPoint;
            this.startIndexM1 = startIndex - 1;
            this.endPointIdx = endIndex - startIndex + 2;
            this.size = endIndex - startIndex + 3;
            //MLog.test.vOnce("sub", "path-test", "e:" + endIndex + " s:" + startIndex + " size: " + size);
        }

        @Override
        public Vec2 get(int idx) {

            if (idx == 0) {
                return startPoint;
            } else if (idx == endPointIdx) {
                return endPoint;
            } else {
                /*if (startIndexM1 + idx == 3) {
                    MLog.test.vOnce("sub2", "path-test", "err called 3 :" + idx + ":" + endPointIdx);
                }*/
                return LinePath.this.get(startIndexM1 + idx);
            }
        }

        @Override
        public int size() {

            return size;
        }

    }
}
