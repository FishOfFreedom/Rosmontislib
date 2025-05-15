/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.data;

import com.freefish.rosmontislib.client.particle.advance.base.IParticle;
import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction;
import com.freefish.rosmontislib.client.particle.advance.data.number.color.Gradient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector4f;

/**
 * @author KilaBash
 * @date 2023/5/30
 * @implNote ColorOverLifetimeSetting
 */
@OnlyIn(Dist.CLIENT)
public class ColorOverLifetimeSetting extends ToggleGroup {

    public void setColor(NumberFunction color) {
        this.color = color;
    }

    protected NumberFunction color = new Gradient();

    public Vector4f getColor(IParticle particle, float partialTicks) {
        var c =  color.get(particle.getT(partialTicks), () -> particle.getMemRandom(this)).intValue();
        return new Vector4f((c >> 16 & 0xff) / 255f, (c >> 8 & 0xff) / 255f, (c & 0xff) / 255f, (c >> 24 & 0xff) / 255f);
    }

}
