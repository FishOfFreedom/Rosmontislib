/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.base.beam;

import com.freefish.rosmontislib.client.particle.advance.AdvancedRLParticleBase;
import com.freefish.rosmontislib.client.particle.advance.RLParticleQueueRenderType;
import com.freefish.rosmontislib.client.particle.advance.RLParticleRenderType;
import com.freefish.rosmontislib.client.particle.advance.data.material.CustomShaderMaterial;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;

@ParametersAreNonnullByDefault
public class RLBeamParticle extends AdvancedRLParticleBase {
    public static int VERSION = 2;

    protected final RLBeamConfig config;

    // runtime
    protected BeamParticle beamParticle;

    public RLBeamParticle(ClientLevel level) {
        this(level,new RLBeamConfig());
        config.material.setMaterial(new CustomShaderMaterial());
    }

    public RLBeamParticle(ClientLevel level, RLBeamConfig config) {
        super(level);
        this.config = config;
        init();
    }

    public void init() {
        beamParticle = new BeamParticle(this, config, getThreadSafeRandomSource());
    }

    //////////////////////////////////////
    //*****     particle logic     *****//
    //////////////////////////////////////

    @Override
    public int getParticleAmount() {
        return beamParticle.isAlive() ? 1 : 0;
    }

    @Override
    public int getLifetime() {
        return config.duration;
    }

    @Override
    protected void updateOrigin() {
        super.updateOrigin();
        setLifetime(config.duration);
    }

    @Override
    public boolean isLooping() {
        return config.isLooping();
    }

    @Override
    public float getT() {
        return t;
    }

    //////////////////////////////////////
    //*****     particle logic     *****//
    //////////////////////////////////////

    @Override
    protected void update() {
        if (beamParticle.isAlive()) {
            beamParticle.tick();
        } else {
            remove();
        }

        super.update();
    }

    @Override
    public void reset() {
        super.reset();
        init();
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, Camera camera, float pPartialTicks) {
        super.render(buffer, camera, pPartialTicks);
        if (!RLParticleQueueRenderType.INSTANCE.isRenderingQueue() && delay <= 0 && isVisible() &&
                RLParticleRenderType.checkLayer(config.renderer.getLayer()) &&
                (!config.renderer.getCull().isEnable() ||
                        RLParticleQueueRenderType.checkFrustum(config.renderer.getCull().getCullAABB(this, pPartialTicks)))) {
            RLParticleQueueRenderType.INSTANCE.pipeQueue(beamParticle.getRenderType(), Collections.singleton(beamParticle), camera, pPartialTicks);
        }
    }

    //////////////////////////////////////
    //********      Emitter    *********//
    //////////////////////////////////////

    @Override
    @Nullable
    public AABB getCullBox(float partialTicks) {
        return config.renderer.getCull().isEnable() ? config.renderer.getCull().getCullAABB(this, partialTicks) : null;
    }

    @Override
    public void remove(boolean force) {
        super.remove(force);
        beamParticle.setRemoved(true);
    }
}