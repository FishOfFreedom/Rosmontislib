package com.freefish.rosmontislib.client.particle.advance.data.number;

import net.minecraft.util.RandomSource;
import org.joml.Vector3f;

import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2023/5/30
 * @implNote NumberFunction3
 */
public class NumberFunction3 {

    public NumberFunction x, y, z;

    public NumberFunction3(NumberFunction x, NumberFunction y, NumberFunction z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public NumberFunction3(NumberFunction x) {
        this.x = x;
        this.y = x;
        this.z = x;
    }

    public NumberFunction3(Number x, Number y, Number z) {
        this.x = NumberFunction.constant(x);
        this.y = NumberFunction.constant(y);
        this.z = NumberFunction.constant(z);
    }

    public NumberFunction3(Number x) {
        this.x = NumberFunction.constant(x);
        this.y = NumberFunction.constant(x);
        this.z = NumberFunction.constant(x);
    }

    public Vector3f get(RandomSource randomSource, float t) {
        return new Vector3f(x.get(randomSource, t).floatValue(), y.get(randomSource, t).floatValue(), z.get(randomSource, t).floatValue());
    }

    public Vector3f get(float t, Supplier<Float> lerp) {
        return new Vector3f(x.get(t, lerp).floatValue(), y.get(t, lerp).floatValue(), z.get(t, lerp).floatValue());
    }

}
