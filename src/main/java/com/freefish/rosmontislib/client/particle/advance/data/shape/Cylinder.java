package com.freefish.rosmontislib.client.particle.advance.data.shape;

import com.freefish.rosmontislib.client.particle.advance.base.IParticleEmitter;
import com.freefish.rosmontislib.client.particle.advance.base.particle.TileParticle;
import com.freefish.rosmontislib.client.utils.Vector3fHelper;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class Cylinder implements IShape {

    private float radius = .5f;

    public float getRadiusThickness() {
        return radiusThickness;
    }

    public void setRadiusThickness(float radiusThickness) {
        this.radiusThickness = radiusThickness;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getArc() {
        return arc;
    }

    public void setArc(float arc) {
        this.arc = arc;
    }

    private float radiusThickness = 1;
    private float arc = 360;


    @Override
    public void nextPosVel(TileParticle particle, IParticleEmitter emitter, Vector3f position, Vector3f rotation, Vector3f scale) {
        var random = particle.getRandomSource();
        var outer = radius;
        var inner = (1 - radiusThickness) * radius;
        var origin = inner * inner;
        var bound = outer * outer;
        var r = outer == inner ? outer : Math.sqrt(origin + random.nextDouble() * (bound - origin));

        var theta = arc * Mth.TWO_PI * random.nextDouble() / 360;

        var pos = new Vector3f((float) (r * Math.cos(theta)),
                random.nextFloat() * scale.y - scale.y / 2,
                (float) (r * Math.sin(theta))).mul(scale);

        particle.setLocalPos(Vector3fHelper.rotateYXY(new Vector3f(pos), rotation).add(position).add(particle.getLocalPos()), true);
        particle.setInternalVelocity(Vector3fHelper.rotateYXY(new Vector3f(pos).normalize().mul(0.05f), rotation));
    }
}