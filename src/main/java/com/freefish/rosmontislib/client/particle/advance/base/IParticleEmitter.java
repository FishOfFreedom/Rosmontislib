/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.base;

import com.freefish.rosmontislib.client.particle.advance.AdvancedRLParticleBase;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.annotation.Nullable;
import java.util.function.Function;


/**
 * @author KilaBash
 * @date 2023/6/2
 * @implNote IParticleEmitter
 */
@OnlyIn(Dist.CLIENT)
public interface IParticleEmitter extends IFXObject {

    default AdvancedRLParticleBase self() {
        return (AdvancedRLParticleBase) this;
    }

    /**
     * get amount of existing particle which emitted from it.
     */
    int getParticleAmount();

    Vector3f getVelocity();

    /**
     * get the box of cull.
     * <br>
     * return null - culling disabled.
     */
    @Nullable
    default AABB getCullBox(float partialTicks) {
        return null;
    }

    int getAge();

    void setAge(int age);

    boolean isLooping();

    void setRGBAColor(Vector4f color);

    Vector4f getRGBAColor();

    float getT();

    float getT(float partialTicks);

    float getMemRandom(Object object);

    float getMemRandom(Object object, Function<RandomSource, Float> randomFunc);

    RandomSource getRandomSource();

    int getLightColor(BlockPos pos);
}
