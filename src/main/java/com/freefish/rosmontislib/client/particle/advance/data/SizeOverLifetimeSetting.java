/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.data;

import com.freefish.rosmontislib.client.particle.advance.base.IParticle;
import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

/**
 * @author KilaBash
 * @date 2023/5/30
 * @implNote SizeOverLifetimeSetting
 */
@OnlyIn(Dist.CLIENT)
public class SizeOverLifetimeSetting extends ToggleGroup {

    public Vector3f getSize(IParticle particle, float partialTicks) {
        return size.get(particle.getT(partialTicks), () -> particle.getMemRandom("sol0"));
    }

    public void setSize(NumberFunction3 size) {
        this.size = size;
    }

    protected NumberFunction3 size = new NumberFunction3(1,1,1);

    public Vector3f getSize(Vector3f startedSize, IParticle particle, float partialTicks) {
        return size.get(particle.getT(partialTicks), () -> particle.getMemRandom("sol0"));
    }

}
