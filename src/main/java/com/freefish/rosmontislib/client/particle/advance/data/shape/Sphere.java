/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.data.shape;

import com.freefish.rosmontislib.client.particle.advance.base.IParticleEmitter;
import com.freefish.rosmontislib.client.particle.advance.base.particle.TileParticle;
import com.freefish.rosmontislib.client.utils.Vector3fHelper;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

/**
 * @author KilaBash
 * @date 2023/5/26
 * @implNote Sphere
 */
public class Sphere implements IShape {
    private float radius = .5f;
    private float radiusThickness = 1;
    private float arc = 360;

    public float getRadiusThickness() {
        return radiusThickness;
    }

    public void setRadiusThickness(float radiusThickness) {
        this.radiusThickness = radiusThickness;
    }

    public float getArc() {
        return arc;
    }

    public void setArc(float arc) {
        this.arc = arc;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }


    @Override
    public void nextPosVel(TileParticle particle, IParticleEmitter emitter, Vector3f position, Vector3f rotation, Vector3f scale) {
        var random = particle.getRandomSource();
        var outer = radius;
        var inner = (1 - radiusThickness) * radius;
        var origin = inner * inner * inner;
        var bound = outer * outer * outer;
        var r = outer == inner ? outer : Math.cbrt(origin + random.nextDouble() * (bound - origin));

        var theta = Math.acos(2 * random.nextDouble() - 1);
        var phi = arc * Mth.TWO_PI * random.nextDouble() / 360;

        var pos = new Vector3f((float) (r * Math.sin(theta) * Math.cos(phi)),
                (float) (r * Math.sin(theta) * Math.sin(phi)),
                (float) (r * Math.cos(theta))).mul(scale);

        particle.setLocalPos(Vector3fHelper.rotateYXY(new Vector3f(pos), rotation).add(position).add(particle.getLocalPos()), true);
        particle.setInternalVelocity(Vector3fHelper.rotateYXY(new Vector3f(pos).normalize().mul(0.05f), rotation));
    }
}
