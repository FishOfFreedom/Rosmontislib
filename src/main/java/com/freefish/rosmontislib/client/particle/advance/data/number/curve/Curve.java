/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.data.number.curve;

import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction;
import net.minecraft.util.RandomSource;

import java.util.function.Supplier;

public class Curve implements NumberFunction {

    private float min;

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(float defaultValue) {
        this.defaultValue = defaultValue;
    }

    public ECBCurves getCurves() {
        return curves;
    }

    public void setCurves(ECBCurves curves) {
        this.curves = curves;
    }

    public String getxAxis() {
        return xAxis;
    }

    public void setxAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public String getyAxis() {
        return yAxis;
    }

    public void setyAxis(String yAxis) {
        this.yAxis = yAxis;
    }

    public float getLower() {
        return lower;
    }

    public void setLower(float lower) {
        this.lower = lower;
    }

    public float getUpper() {
        return upper;
    }

    public void setUpper(float upper) {
        this.upper = upper;
    }

    private float max;
    private float defaultValue;

    private ECBCurves curves;

    private String xAxis, yAxis;

    public boolean isLockControlPoint() {
        return lockControlPoint;
    }

    public void setLockControlPoint(boolean lockControlPoint) {
        this.lockControlPoint = lockControlPoint;
    }

    protected boolean lockControlPoint = true;

    private float lower, upper;

    public Curve() {
        this(0, 0, 0, 0, 0, "", "");
    }

    public Curve(float min, float max, float lower, float upper, float defaultValue, String xAxis, String yAxis) {
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
        this.lower = lower;
        this.upper = upper;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        var y = (upper == lower) ? 0.5f : (defaultValue - lower) / (upper - lower);
        this.curves = new ECBCurves(0, y, 0.1f, y, 0.9f, y, 1, y);
    }

    @Override
    public Number get(RandomSource randomSource, float t) {
        return lower + (upper - lower) * curves.getCurveY(t);
    }

    @Override
    public Number get(float t, Supplier<Float> lerp) {
        return lower + (upper - lower) * curves.getCurveY(t);
    }
}