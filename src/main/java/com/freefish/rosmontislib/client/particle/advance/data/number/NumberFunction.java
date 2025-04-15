package com.freefish.rosmontislib.client.particle.advance.data.number;

import com.freefish.rosmontislib.client.particle.advance.data.number.color.Color;
import net.minecraft.util.RandomSource;

import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2023/5/25
 * @implNote NumberFunction
 */
public interface NumberFunction {

    static NumberFunction constant(Number constant) {
        return new Constant(constant);
    }

    static NumberFunction color(Number color) {
        return new Color(color);
    }

    default Number get(RandomSource randomSource, float t) {
        return get(t, randomSource::nextFloat);
    }

    Number get(float t, Supplier<Float> lerp);

}
