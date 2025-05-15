/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.data.number.curve;

import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction;
import net.minecraft.util.RandomSource;

import java.util.function.Supplier;

public class Line implements NumberFunction {
    private final float[] timeline;
    private final float[] timelineValue;

    public Line(float[] timeline,float[] timelineValue){
        this.timeline = timeline;
        this.timelineValue = timelineValue;
    }

    @Override
    public Number get(RandomSource randomSource, float t) {
        if (timelineValue.length != timeline.length) return 0;
        for (int i = 0; i < timeline.length; i++) {
            float time = timeline[i];
            if (t == time) return timelineValue[i];
            else if (t < time) {
                if (i == 0) return timelineValue[0];
                float a = (t - timeline[i - 1]) / (time - timeline[i - 1]);
                return timelineValue[i - 1] * (1 - a) + timelineValue[i] * a;
            }
            else {
                if (i == timelineValue.length - 1) return timelineValue[i];
            }
        }
        return 0;
    }

    @Override
    public Number get(float t, Supplier<Float> lerp) {
        if (timelineValue.length != timeline.length) return 0;
        for (int i = 0; i < timeline.length; i++) {
            float time = timeline[i];
            if (t == time) return timelineValue[i];
            else if (t < time) {
                if (i == 0) return timelineValue[0];
                float a = (t - timeline[i - 1]) / (time - timeline[i - 1]);
                return timelineValue[i - 1] * (1 - a) + timelineValue[i] * a;
            }
            else {
                if (i == timelineValue.length - 1) return timelineValue[i];
            }
        }
        return 0;
    }
}
