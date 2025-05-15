/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.data.number.curve;

import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction;

import java.util.function.Supplier;

public class RandomLine implements NumberFunction {
    private final float[] timeline;
    private final float[] timelineValueUp;
    private final float[] timelineValueDown;

    public RandomLine(float[] timeline,float[] timelineValueUp,float[] timelineValueDown) {
        this.timeline = timeline;
        this.timelineValueUp = timelineValueUp;
        this.timelineValueDown = timelineValueDown;
    }

    @Override
    public Number get(float t, Supplier<Float> lerp) {
        if (timelineValueUp.length != timeline.length) return 0;
        for (int i = 0; i < timeline.length; i++) {
            float time = timeline[i];
            if (t == time) return getRandomValue(lerp,timelineValueUp[i],timelineValueDown[i]);
            else if (t < time) {
                if (i == 0) return getRandomValue(lerp,timelineValueUp[0],timelineValueDown[0]);
                float a = (t - timeline[i - 1]) / (time - timeline[i - 1]);
                return getRandomValue(lerp,timelineValueUp[i - 1] * (1 - a) + timelineValueUp[i] * a,timelineValueDown[i - 1] * (1 - a) + timelineValueDown[i] * a);
            }
            else {
                if (i == timelineValueUp.length - 1) return getRandomValue(lerp,timelineValueUp[i],timelineValueDown[i]);;
            }
        }
        return 0;
    }

    private Number getRandomValue(Supplier<Float> lerp,float a, float b){
        return a == b ? a : (Math.min(a, b) + lerp.get() * Math.abs(a - b));
    }
}
