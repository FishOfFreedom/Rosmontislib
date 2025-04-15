package com.freefish.rosmontislib.gui.widget.animation.interpolator;

public class RLinearInterpolator implements RInterpolator {
    @Override
    public float interpolate(float start, float end, float fraction) {
        return start + (end - start) * fraction;
    }
}