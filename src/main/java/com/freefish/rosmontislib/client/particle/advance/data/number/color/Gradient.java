package com.freefish.rosmontislib.client.particle.advance.data.number.color;

import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction;
import com.freefish.rosmontislib.client.utils.GradientColor;
import net.minecraft.util.RandomSource;

import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2023/5/26
 * @implNote Gradient
 */
public class Gradient implements NumberFunction {

    private final GradientColor gradientColor;

    public Gradient() {
        this.gradientColor = new GradientColor();
    }

    public Gradient(int color) {
        this.gradientColor = new GradientColor(color, color);
    }

    public Gradient(GradientColor gradientColor) {
        this.gradientColor = gradientColor;
    }

    @Override
    public Number get(RandomSource randomSource, float t) {
        return gradientColor.getColor(t);
    }

    @Override
    public Number get(float t, Supplier<Float> lerp) {
        return gradientColor.getColor(t);
    }

    public GradientColor getGradientColor() {
        return gradientColor;
    }
}
