/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.data;

import com.freefish.rosmontislib.client.particle.advance.base.IParticle;
import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

/**
 * @author KilaBash
 * @date 2023/5/30
 * @implNote RotationOverLifetimeSetting
 */
@OnlyIn(Dist.CLIENT)
public class RotationOverLifetimeSetting extends ToggleGroup {
    protected NumberFunction roll = NumberFunction.constant(0);
    protected NumberFunction pitch = NumberFunction.constant(0);
    protected NumberFunction yaw = NumberFunction.constant(0);

    public NumberFunction getRoll() {
        return roll;
    }

    public void setRoll(NumberFunction roll) {
        this.roll = roll;
    }

    public NumberFunction getPitch() {
        return pitch;
    }

    public void setPitch(NumberFunction pitch) {
        this.pitch = pitch;
    }

    public NumberFunction getYaw() {
        return yaw;
    }

    public void setYaw(NumberFunction yaw) {
        this.yaw = yaw;
    }

    public Vector3f getRotation(IParticle particle, float partialTicks) {
        var t = particle.getT(partialTicks);
        return new Vector3f(
                roll.get(t, () -> particle.getMemRandom("rol0")).floatValue(),
                pitch.get(t, () -> particle.getMemRandom("rol1")).floatValue(),
                yaw.get(t, () -> particle.getMemRandom("rol2")).floatValue()).mul(Mth.TWO_PI / 360);
    }

}
