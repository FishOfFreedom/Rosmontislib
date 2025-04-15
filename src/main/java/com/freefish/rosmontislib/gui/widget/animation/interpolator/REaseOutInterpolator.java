package com.freefish.rosmontislib.gui.widget.animation.interpolator;

public class REaseOutInterpolator implements RInterpolator {
    @Override
    public float interpolate(float start, float end, float fraction) {
        return start + (end - start) * (float) Math.sin(fraction * Math.PI / 2);
    }
}