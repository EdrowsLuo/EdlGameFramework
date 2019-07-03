package com.edlplan.framework.easing;

import com.edlplan.framework.easing.Easing;
import com.edlplan.framework.math.FMath;

public class EasingManager {
    static final double elastic_const = 2 * Math.PI / .3;
    static final double elastic_const2 = .3 / 4;

    static final double back_const = 1.70158;
    static final double back_const2 = back_const * 1.525;

    static final double bounce_const = 1 / 2.75;

    public static final boolean ENABLE_EASING = true;

    public static EasingInterpolator[] easingInterpolators = new EasingInterpolator[Easing.values().length];

    static {
        for (int i = 0; i < easingInterpolators.length; i++) {
            easingInterpolators[i] = toInterpolator(Easing.getEasing(i));
        }
    }

    public static double apply(Easing easing, double v) {
        return easingInterpolators[easing.id].interpolate((float) v);
        /*if (ENABLE_EASING) {
            switch (easing) {
                case None:
                    return v;
                case In:
                case InQuad:
                    return v * v;
                case Out:
                case OutQuad:
                    return v * (2 - v);
                case InOutQuad:
                    if (v < .5) return v * v * 2;
                    return --v * v * -2 + 1;
                case InCubic:
                    return v * v * v;
                case OutCubic:
                    return --v * v * v + 1;
                case InOutCubic:
                    if (v < .5) return v * v * v * 4;
                    return --v * v * v * 4 + 1;

                case InQuart:
                    return v * v * v * v;
                case OutQuart:
                    return 1 - --v * v * v * v;
                case InOutQuart:
                    if (v < .5) return v * v * v * v * 8;
                    return --v * v * v * v * -8 + 1;

                case InQuint:
                    return v * v * v * v * v;
                case OutQuint:
                    return --v * v * v * v * v + 1;
                case InOutQuint:
                    if (v < .5) return v * v * v * v * v * 16;
                    return --v * v * v * v * v * 16 + 1;

                case InSine:
                    return 1 - Math.cos(v * Math.PI * .5);
                case OutSine:
                    return Math.sin(v * Math.PI * .5);
                case InOutSine:
                    return .5 - .5 * Math.cos(Math.PI * v);

                case InExpo:
                    return Math.pow(2, 10 * (v - 1));
                case OutExpo:
                    return -Math.pow(2, -10 * v) + 1;
                case InOutExpo:
                    if (v < .5) return .5 * Math.pow(2, 20 * v - 10);
                    return 1 - .5 * Math.pow(2, -20 * v + 10);

                case InCirc:
                    return 1 - Math.sqrt(1 - v * v);
                case OutCirc:
                    return Math.sqrt(1 - --v * v);
                case InOutCirc:
                    if ((v *= 2) < 1) return .5 - .5 * Math.sqrt(1 - v * v);
                    return .5 * Math.sqrt(1 - (v -= 2) * v) + .5;

                case InElastic:
                    return -Math.pow(2, -10 + 10 * v) * Math.sin((1 - elastic_const2 - v) * elastic_const);
                case OutElastic:
                    return Math.pow(2, -10 * v) * Math.sin((v - elastic_const2) * elastic_const) + 1;
                case OutElasticHalf:
                    return Math.pow(2, -10 * v) * Math.sin((.5 * v - elastic_const2) * elastic_const) + 1;
                case OutElasticQuarter:
                    return Math.pow(2, -10 * v) * Math.sin((.25 * v - elastic_const2) * elastic_const) + 1;
                case InOutElastic:
                    if ((v *= 2) < 1)
                        return -.5 * Math.pow(2, -10 + 10 * v) * Math.sin((1 - elastic_const2 * 1.5 - v) * elastic_const / 1.5);
                    return .5 * Math.pow(2, -10 * --v) * Math.sin((v - elastic_const2 * 1.5) * elastic_const / 1.5) + 1;

                case InBack:
                    return v * v * ((back_const + 1) * v - back_const);
                case OutBack:
                    return --v * v * ((back_const + 1) * v + back_const) + 1;
                case InOutBack:
                    if ((v *= 2) < 1) return .5 * v * v * ((back_const2 + 1) * v - back_const2);
                    return .5 * ((v -= 2) * v * ((back_const2 + 1) * v + back_const2) + 2);

                case InBounce:
                    v = 1 - v;
                    if (v < bounce_const)
                        return 1 - 7.5625 * v * v;
                    if (v < 2 * bounce_const)
                        return 1 - (7.5625 * (v -= 1.5 * bounce_const) * v + .75);
                    if (v < 2.5 * bounce_const)
                        return 1 - (7.5625 * (v -= 2.25 * bounce_const) * v + .9375);
                    return 1 - (7.5625 * (v -= 2.625 * bounce_const) * v + .984375);
                case OutBounce:
                    if (v < bounce_const)
                        return 7.5625 * v * v;
                    if (v < 2 * bounce_const)
                        return 7.5625 * (v -= 1.5 * bounce_const) * v + .75;
                    if (v < 2.5 * bounce_const)
                        return 7.5625 * (v -= 2.25 * bounce_const) * v + .9375;
                    return 7.5625 * (v -= 2.625 * bounce_const) * v + .984375;
                case InOutBounce:
                    if (v < .5) return .5 - .5 * apply(Easing.OutBounce, 1 - v * 2);
                    return apply(Easing.OutBounce, (v - .5) * 2) * .5 + .5;

                case OutPow10:
                    return --v * Math.pow(v, 10) + 1;
                default:
                    return v;
            }
        } else {
            return v;
        }*/
    }

   private static EasingInterpolator toInterpolator(Easing easing) {
        if (ENABLE_EASING) {
            switch (easing) {
                case None:
                    return v -> v;
                case In:
                case InQuad:
                    return v -> v * v;
                case Out:
                case OutQuad:
                    return v -> v * (2 - v);
                case InOutQuad:
                    return v -> v < .5 ? (v * v * 2) : (--v * v * -2 + 1);
                case InCubic:
                    return v -> v * v * v;
                case OutCubic:
                    return v -> --v * v * v + 1;
                case InOutCubic:
                    return v -> v < .5 ? (v * v * v * 4) : (--v * v * v * 4 + 1);
                case InQuart:
                    return v -> v * v * v * v;
                case OutQuart:
                    return v -> 1 - --v * v * v * v;
                case InOutQuart:
                    return v -> v < .5 ? (v * v * v * v * 8) : (--v * v * v * v * -8 + 1);
                case InQuint:
                    return v -> v * v * v * v * v;
                case OutQuint:
                    return v -> --v * v * v * v * v + 1;
                case InOutQuint:
                    return v -> v < .5?( v * v * v * v * v * 16):(--v * v * v * v * v * 16 + 1);
                case InSine:
                    return v -> (float) (1 - Math.cos(v * Math.PI * .5));
                case OutSine:
                    return v -> (float) Math.sin(v * Math.PI * .5);
                case InOutSine:
                    return v -> (float) (.5f - .5f * Math.cos(Math.PI * v));
                case InExpo:
                    return v -> (float) Math.pow(2, 10 * (v - 1));
                case OutExpo:
                    return v -> (float) (-Math.pow(2, -10 * v) + 1);
                case InOutExpo:
                    return v -> (float) (v < .5 ? (.5 * Math.pow(2, 20 * v - 10)) : (1 - .5 * Math.pow(2, -20 * v + 10)));
                case InCirc:
                    return v -> (float) (1 - Math.sqrt(1 - v * v));
                case OutCirc:
                    return v -> (float) Math.sqrt(1 - --v * v);
                case InOutCirc:
                    return v -> {
                        if ((v *= 2) < 1) return (float) (.5 - .5 * Math.sqrt(1 - v * v));
                        return (float) (.5 * Math.sqrt(1 - (v -= 2) * v) + .5);
                    };
                case InElastic:
                    return v -> (float) (-Math.pow(2, -10 + 10 * v) * Math.sin((1 - elastic_const2 - v) * elastic_const));
                case OutElastic:
                    return v -> (float) (Math.pow(2, -10 * v) * Math.sin((v - elastic_const2) * elastic_const) + 1);
                case OutElasticHalf:
                    return v -> (float) (Math.pow(2, -10 * v) * Math.sin((.5 * v - elastic_const2) * elastic_const) + 1);
                case OutElasticQuarter:
                    return v -> (float) (Math.pow(2, -10 * v) * Math.sin((.25 * v - elastic_const2) * elastic_const) + 1);
                case InOutElastic:
                    return v -> {
                        if ((v *= 2) < 1)
                            return (float) (-.5 * Math.pow(2, -10 + 10 * v) * Math.sin((1 - elastic_const2 * 1.5 - v) * elastic_const / 1.5));
                        return (float) (.5 * Math.pow(2, -10 * --v) * Math.sin((v - elastic_const2 * 1.5) * elastic_const / 1.5) + 1);
                    };
                case InBack:
                    return v -> (float) (v * v * ((back_const + 1) * v - back_const));
                case OutBack:
                    return v -> (float) (--v * v * ((back_const + 1) * v + back_const) + 1);
                case InOutBack:
                    return v -> {
                        if ((v *= 2) < 1)
                            return (float) (.5 * v * v * ((back_const2 + 1) * v - back_const2));
                        return (float) (.5 * ((v -= 2) * v * ((back_const2 + 1) * v + back_const2) + 2));
                    };
                case InBounce:
                    return v -> {
                        v = 1 - v;
                        if (v < bounce_const)
                            return (float) (1 - 7.5625 * v * v);
                        if (v < 2 * bounce_const)
                            return (float) (1 - (7.5625 * (v -= 1.5 * bounce_const) * v + .75));
                        if (v < 2.5 * bounce_const)
                            return (float) (1 - (7.5625 * (v -= 2.25 * bounce_const) * v + .9375));
                        return (float) (1 - (7.5625 * (v -= 2.625 * bounce_const) * v + .984375));
                    };
                case OutBounce:
                    return v -> {
                        if (v < bounce_const)
                            return (float) (7.5625 * v * v);
                        if (v < 2 * bounce_const)
                            return (float) (7.5625 * (v -= 1.5 * bounce_const) * v + .75);
                        if (v < 2.5 * bounce_const)
                            return (float) (7.5625 * (v -= 2.25 * bounce_const) * v + .9375);
                        return (float) (7.5625 * (v -= 2.625 * bounce_const) * v + .984375);
                    };
                case InOutBounce:
                    return v -> {
                        if (v < .5) return (float) (.5 - .5 * apply(Easing.OutBounce, 1 - v * 2));
                        return (float) (apply(Easing.OutBounce, (v - .5) * 2) * .5 + .5);
                    };
                case OutPow10:
                    return v -> (float) (--v * Math.pow(v, 10) + 1);
                default:
                    return v -> v;
            }

        } else {
            return v -> v;
        }
    }
}
