package com.freefish.rosmontislib.gui.widget.animation;

import com.freefish.rosmontislib.gui.widget.animation.interpolator.RInterpolator;

import java.util.function.Consumer;

public class RKeyValue {
    private final Consumer<Float> propertySetter; // 属性设置器
    private final float targetValue;             // 目标值
    private final RInterpolator interpolator;      // 插值方式

    public RKeyValue(Consumer<Float> propertySetter, float targetValue, RInterpolator interpolator) {
        this.propertySetter = propertySetter;
        this.targetValue = targetValue;
        this.interpolator = interpolator;
    }

    public void update(float startValue, float progress) {
        float value = interpolator.interpolate(startValue, targetValue, progress);
        propertySetter.accept(value);
    }
}