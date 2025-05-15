/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.data.number.color;

import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction;
import com.freefish.rosmontislib.client.utils.ColorUtils;
import com.freefish.rosmontislib.client.utils.GradientColor;

import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2023/5/26
 * @implNote RandomGradient
 */
public class RandomGradient implements NumberFunction {

    public GradientColor getGradientColor0() {
        return gradientColor0;
    }

    private final GradientColor gradientColor0;

    public GradientColor getGradientColor1() {
        return gradientColor1;
    }

    private final GradientColor gradientColor1;

    public RandomGradient() {
        this.gradientColor0 = new GradientColor();
        this.gradientColor1 = new GradientColor();
    }

    public RandomGradient(int color) {
        this.gradientColor0 = new GradientColor(color, color);
        this.gradientColor1 = new GradientColor(color, color);
    }

    @Override
    public Number get(float t, Supplier<Float> lerp) {
        int color0 = gradientColor0.getColor(t);
        int color1 = gradientColor1.getColor(t);
        return ColorUtils.blendColor(color0, color1, lerp.get());
    }
}
