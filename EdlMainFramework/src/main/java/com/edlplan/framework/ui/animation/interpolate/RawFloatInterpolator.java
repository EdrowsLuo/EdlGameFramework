package com.edlplan.framework.ui.animation.interpolate;

import com.edlplan.framework.easing.Easing;
import com.edlplan.framework.easing.EasingManager;

public class RawFloatInterpolator {
    public static RawFloatInterpolator Instance = new RawFloatInterpolator();

    public float applyInterplate(Float startValue, Float endValue, double time, Easing easing) {

        double inp = EasingManager.apply(easing, time);
        return (float) (startValue * (1 - inp) + endValue * inp);
    }
}
