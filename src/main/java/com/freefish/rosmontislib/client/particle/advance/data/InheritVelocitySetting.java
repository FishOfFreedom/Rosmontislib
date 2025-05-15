/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.data;

import com.freefish.rosmontislib.client.particle.advance.AdvancedRLParticleBase;
import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

/**
 * @author KilaBash
 * @date 2023/5/30
 * @implNote InheritVelocitySetting
 */
@OnlyIn(Dist.CLIENT)
public class InheritVelocitySetting extends ToggleGroup {
    public enum Mode {
        CURRENT,
        INITIAL,
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public NumberFunction getMultiply() {
        return multiply;
    }

    public void setMultiply(NumberFunction multiply) {
        this.multiply = multiply;
    }

    protected Mode mode = Mode.INITIAL;

    protected NumberFunction multiply = NumberFunction.constant(1);

    public Vector3f getVelocity(AdvancedRLParticleBase emitter) {
        return emitter.getVelocity().mul(multiply.get(emitter.getT(), () -> emitter.getMemRandom(this)).floatValue());
    }

}
