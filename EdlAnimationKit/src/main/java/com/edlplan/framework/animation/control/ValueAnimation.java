package com.edlplan.framework.animation.control;

import com.edlplan.framework.animation.AnimationClock;


/**
 *
 *
 *
 *
 *
 */


public interface ValueAnimation {

    enum UpdateType {
        ByLifeTime,//根据clock的时间线来更新的动画，不会受到
        ByDeltaTime,
    }

    void update(AnimationClock clock);



}
