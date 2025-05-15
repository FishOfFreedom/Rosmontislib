/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.data.number;

import java.util.function.Supplier;

public class RandomConstant implements NumberFunction {
    private Number a;

    public Number getA() {
        return a;
    }

    public void setA(Number a) {
        this.a = a;
    }

    public Number getB() {
        return b;
    }

    public void setB(Number b) {
        this.b = b;
    }

    public boolean isDecimals() {
        return isDecimals;
    }

    public void setDecimals(boolean decimals) {
        isDecimals = decimals;
    }

    private Number b;

    private boolean isDecimals;

    public RandomConstant() {
        a = 0;
        b = 0;
    }

    public RandomConstant(Number a, Number b, boolean isDecimals) {
        this.a = a;
        this.b = b;
        this.isDecimals = isDecimals;
    }

    @Override
    public Number get(float t, Supplier<Float> lerp) {
        float min = Math.min(a.floatValue(), b.floatValue());
        float max = Math.max(a.floatValue(), b.floatValue());
        if (min == max) return max;
        if (isDecimals) return (min + lerp.get() * (max - min));
        return (int)(min + lerp.get() * (max + 1 - min));
    }
}