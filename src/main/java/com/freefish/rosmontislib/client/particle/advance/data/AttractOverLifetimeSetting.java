/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.data;

import com.freefish.rosmontislib.client.particle.advance.base.IParticle;
import com.freefish.rosmontislib.client.particle.advance.base.particle.TileParticle;
import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction;
import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction3;
import com.freefish.rosmontislib.client.utils.Vector3fHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

/**
 * @author KilaBash
 * @date 2023/5/30
 * @implNote VelocityOverLifetimeSetting
 */
@OnlyIn(Dist.CLIENT)
public class AttractOverLifetimeSetting extends ToggleGroup{

    protected NumberFunction attraction = NumberFunction.constant(0);

    protected NumberFunction3 offset = new NumberFunction3(0, 0, 0);

    public NumberFunction3 getOffset() {
        return offset;
    }

    public void setOffset(NumberFunction3 offset) {
        this.offset = offset;
    }

    public NumberFunction getAttraction() {
        return attraction;
    }

    public void setAttraction(NumberFunction attraction) {
        this.attraction = attraction;
    }

    public void getVelocityAddition(TileParticle particle) {
        var lifetime = particle.getT();
        var center = offset.get(lifetime, () -> particle.getMemRandom("vol2"));
        float v = attraction.get(particle.getRandomSource(), particle.getT()).floatValue() * 0.5f;

        var toPoint = particle.getLocalPos().sub(center);

    }
}
