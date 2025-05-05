package com.freefish.rosmontislib.client.particle.advance.base.trail;

import com.freefish.rosmontislib.client.particle.advance.AdvancedRLParticleBase;
import com.freefish.rosmontislib.client.particle.advance.RLParticleQueueRenderType;
import com.freefish.rosmontislib.client.particle.advance.RLParticleRenderType;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;

@ParametersAreNonnullByDefault
public class RLTrailParticle extends AdvancedRLParticleBase {
    public static int VERSION = 2;

    public final RLTrailConfig config;

    // runtime
    protected TrailParticle trailParticle;

    public RLTrailParticle(ClientLevel level) {
        this(level,new RLTrailConfig());
        config.smoothInterpolation = true;
        config.minVertexDistance = 0.1f;
    }

    public RLTrailParticle(ClientLevel level, RLTrailConfig config) {
        super(level);
        this.config = config;
        init();
    }

    public void init() {
        trailParticle = new TrailParticle(this, config, getThreadSafeRandomSource());
    }

    //////////////////////////////////////
    //*****     particle logic     *****//
    //////////////////////////////////////

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

    @Override
    public int getParticleAmount() {
        return trailParticle.isAlive() ? 1 : 0;
    }

    @Override
    protected void update() {
        if (trailParticle.isAlive()) {
            trailParticle.tick();
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
                        RLParticleRenderType.checkFrustum(config.renderer.getCull().getCullAABB(this, pPartialTicks)))) {
            RLParticleQueueRenderType.INSTANCE.pipeQueue(trailParticle.getRenderType(), Collections.singleton(trailParticle), camera, pPartialTicks);
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
        trailParticle.setRemoved(true);
        super.remove(force);
        if (force) {
            trailParticle.getTails().clear();
        }
    }
}