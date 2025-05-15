/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.data;

import com.freefish.rosmontislib.client.particle.advance.base.IParticleEmitter;
import com.freefish.rosmontislib.client.particle.advance.base.particle.TileParticle;
import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction3;
import com.freefish.rosmontislib.client.particle.advance.data.shape.Cone;
import com.freefish.rosmontislib.client.particle.advance.data.shape.IShape;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

/**
 * @author KilaBash
 * @date 2023/5/27
 * @implNote Shape
 */
@OnlyIn(Dist.CLIENT)
public class ShapeSetting {

    private IShape shape = new Cone();

    private NumberFunction3 position = new NumberFunction3(0 ,0, 0);

    private NumberFunction3 rotation = new NumberFunction3(0 ,0, 0);

    private NumberFunction3 scale = new NumberFunction3(1, 1, 1);

    public NumberFunction3 getPosition() {
        return position;
    }

    public void setPosition(NumberFunction3 position) {
        this.position = position;
    }

    public NumberFunction3 getScale() {
        return scale;
    }

    public void setScale(NumberFunction3 scale) {
        this.scale = scale;
    }

    public NumberFunction3 getRotation() {
        return rotation;
    }

    public void setRotation(NumberFunction3 rotation) {
        this.rotation = rotation;
    }

    public IShape getShape() {
        return shape;
    }

    public void setShape(IShape shape) {
        this.shape = shape;
    }

    public void setupParticle(TileParticle particle, IParticleEmitter emitter) {
        var t = emitter.getT();
        shape.nextPosVel(particle, emitter,
                position.get(t, () -> emitter.getMemRandom("shape_position")),
                new Vector3f(rotation.get(t, () -> emitter.getMemRandom("shape_rotation")).mul(Mth.TWO_PI / 360)),
                new Vector3f(scale.get(t, () -> emitter.getMemRandom("shape_scale"))));
    }
}