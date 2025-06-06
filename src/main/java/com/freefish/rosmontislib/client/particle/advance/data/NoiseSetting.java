/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.data;

import com.freefish.rosmontislib.client.particle.advance.base.IParticle;
import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction;
import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction3;
import com.freefish.rosmontislib.client.utils.noise.PerlinNoise;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;


/**
 * @author KilaBash
 * @date 2023/5/31
 * @implNote NoiseSetting
 */
@OnlyIn(Dist.CLIENT)
public class NoiseSetting  extends ToggleGroup{
    public enum Quality {
        Noise1D,
        Noise2D,
        Noise3D
    }

    private final ThreadLocal<PerlinNoise> noise = ThreadLocal.withInitial(PerlinNoise::new);

    protected float frequency = 1;

    protected Quality quality = Quality.Noise2D;

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public NumberFunction3 getPosition() {
        return position;
    }

    public void setPosition(NumberFunction3 position) {
        this.position = position;
    }

    public NumberFunction getRotation() {
        return rotation;
    }

    public void setRotation(NumberFunction rotation) {
        this.rotation = rotation;
    }

    public NumberFunction getSize() {
        return size;
    }

    public void setSize(NumberFunction size) {
        this.size = size;
    }

    protected NumberFunction3 position = new NumberFunction3(0.1, 0.1, 0.1);

    protected NumberFunction rotation = NumberFunction.constant(0);

    protected NumberFunction size = NumberFunction.constant(0);


    public float getNoise(float t) {
        var input = t * frequency;
        float value = (float)switch (quality) {
            case Noise1D -> noise.get().noise(input);
            case Noise2D -> noise.get().noise(input, input);
            case Noise3D -> noise.get().noise(input, input, input);
        };
        return value;
    }

    public void setupSeed(IParticle particle) {
        noise.get().setSeed(particle.getMemRandom("noise-seed", randomSource -> (float) randomSource.nextGaussian()) * 255);
    }

    public Vector3f getRotation(IParticle particle, float partialTicks) {
        setupSeed(particle);
        var t = particle.getT(partialTicks);
        var degree = rotation.get(t, () -> particle.getMemRandom("noise-rotation")).floatValue();
        if (degree != 0) {
            return new Vector3f(degree, 0, 0).mul(getNoise((t + 10 * particle.getMemRandom("noise-rotation-degree")) * 100) * Mth.TWO_PI / 360);
        }
        return new Vector3f(0 ,0, 0);
    }

    public Vector3f getSize(IParticle particle, float partialTicks) {
        setupSeed(particle);
        var t = particle.getT(partialTicks);
        var scale = size.get(t, () -> particle.getMemRandom("noise-size")).floatValue();
        if (scale != 0) {
            return new Vector3f(scale, scale, scale).mul(getNoise((t + 10 * particle.getMemRandom("noise-size-scale")) * 100));
        }
        return new Vector3f(0 ,0, 0);
    }

    public Vector3f getPosition(IParticle particle, float partialTicks) {
        setupSeed(particle);
        var t = particle.getT(partialTicks);
        var offset = position.get(t, () -> particle.getMemRandom("noise-position"));
        if (!(offset.x == 0 && offset.y == 0 && offset.z == 0)) {
            offset.mul(
                    getNoise((t + 10 * particle.getMemRandom("noise-position-x")) * 100),
                    getNoise((t + 10 * particle.getMemRandom("noise-position-y")) * 100),
                    getNoise((t + 10 * particle.getMemRandom("noise-position-z")) * 100));
            return offset;
        }
        return new Vector3f(0 ,0, 0);
    }
}
