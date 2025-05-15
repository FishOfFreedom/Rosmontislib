/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.base;

import com.freefish.rosmontislib.client.particle.advance.RLParticleRenderType;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.util.RandomSource;

import java.util.function.Function;

public interface IParticle {

    RLParticleRenderType getRenderType();

    RandomSource getRandomSource();

    boolean isRemoved();

    default boolean isAlive() {
        return !isRemoved();
    }

    float getT();

    float getT(float partialTicks);

    float getMemRandom(Object object);

    float getMemRandom(Object object, Function<RandomSource, Float> randomFunc);

    void tick();

    void render(VertexConsumer buffer, Camera camera, float pPartialTicks);
}