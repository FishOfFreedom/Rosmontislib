package com.freefish.rosmontislib.client.particle.advance.data.shape;

import com.freefish.rosmontislib.client.particle.advance.base.IParticleEmitter;
import com.freefish.rosmontislib.client.particle.advance.base.particle.TileParticle;
import org.joml.Vector3f;

/**
 * @author KilaBash
 * @date 2023/5/26
 * @implNote Dot
 */
public class Dot implements IShape {

    @Override
    public void nextPosVel(TileParticle particle, IParticleEmitter emitter, Vector3f position, Vector3f rotation, Vector3f scale) {
        particle.setLocalPos(position.add(particle.getLocalPos()), true);
        particle.setInternalVelocity(new Vector3f(0, 0, 0));
    }
}
