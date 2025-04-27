package com.freefish.rosmontislib.client.particle.advance.data.number.curve;

import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction;

import java.util.function.Supplier;

public class RandomCurve implements NumberFunction {

    private float min;

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(float defaultValue) {
        this.defaultValue = defaultValue;
    }

    public ECBCurves getCurves0() {
        return curves0;
    }

    public void setCurves0(ECBCurves curves0) {
        this.curves0 = curves0;
    }

    public ECBCurves getCurves1() {
        return curves1;
    }

    public void setCurves1(ECBCurves curves1) {
        this.curves1 = curves1;
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

    public boolean isLockControlPoint() {
        return lockControlPoint;
    }

    public void setLockControlPoint(boolean lockControlPoint) {
        this.lockControlPoint = lockControlPoint;
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

    private ECBCurves curves0, curves1;

    private String xAxis, yAxis;

    protected boolean lockControlPoint = true;

    private float lower, upper;

    public RandomCurve() {
        this(0, 0, 0, 0, 0, "", "");
    }

    public RandomCurve(float min, float max, float lower, float upper, float defaultValue, String xAxis, String yAxis) {
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
        this.lower = lower;
        this.upper = upper;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        var y = (upper == lower) ? 0.5f : (defaultValue - lower) / (upper - lower);
        this.curves0 = new ECBCurves(0, y, 0.1f, y, 0.9f, y, 1, y);
        this.curves1 = new ECBCurves(0, y, 0.1f, y, 0.9f, y, 1, y);
    }

    @Override
    public Number get(float t, Supplier<Float> lerp) {
        var a = curves0.getCurveY(t);
        var b = curves1.getCurveY(t);
        var randomY = a == b ? a : (Math.min(a, b) + lerp.get() * Math.abs(a - b));
        return lower + (upper - lower) * randomY;
    }
}