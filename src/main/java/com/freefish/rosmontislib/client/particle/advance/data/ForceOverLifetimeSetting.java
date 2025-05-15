/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.data;

import com.freefish.rosmontislib.client.particle.advance.base.IParticle;
import com.freefish.rosmontislib.client.particle.advance.base.particle.RLParticleConfig;
import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction3;
import org.joml.Vector3f;

/**
 * @author KilaBash
 * @date 2023/5/30
 * @implNote LifetimeByEmitterSpeed
 */
public class ForceOverLifetimeSetting extends ToggleGroup {

    public NumberFunction3 getForce() {
        return force;
    }

    public void setForce(NumberFunction3 force) {
        this.force = force;
    }

    protected NumberFunction3 force = new NumberFunction3(0, 0, 0);

    public RLParticleConfig.Space getSimulationSpace() {
        return simulationSpace;
    }

    public void setSimulationSpace(RLParticleConfig.Space simulationSpace) {
        this.simulationSpace = simulationSpace;
    }

    protected RLParticleConfig.Space simulationSpace = RLParticleConfig.Space.Local;

    public Vector3f getForce(IParticle particle) {
        return force.get(particle.getT(), () -> particle.getMemRandom(this)).mul(0.05f);
    }

}
