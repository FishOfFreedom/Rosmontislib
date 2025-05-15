/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.base.trail;

import com.freefish.rosmontislib.client.particle.advance.RLParticleRenderType;
import com.freefish.rosmontislib.client.particle.advance.data.LightOverLifetimeSetting;
import com.freefish.rosmontislib.client.particle.advance.data.MaterialSetting;
import com.freefish.rosmontislib.client.particle.advance.data.RendererSetting;
import com.freefish.rosmontislib.client.particle.advance.data.UVAnimationSetting;
import com.freefish.rosmontislib.client.particle.advance.data.material.CustomShaderMaterial;
import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction;
import com.freefish.rosmontislib.client.particle.advance.data.number.color.Gradient;
import com.freefish.rosmontislib.mixin.accessor.BlendModeAccessor;
import com.freefish.rosmontislib.mixin.accessor.ShaderInstanceAccessor;
import com.mojang.blaze3d.shaders.BlendMode;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

public class RLTrailConfig {

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public NumberFunction getColorOverTrail() {
        return colorOverTrail;
    }

    public void setColorOverTrail(NumberFunction colorOverTrail) {
        this.colorOverTrail = colorOverTrail;
    }

    public NumberFunction getWidthOverTrail() {
        return widthOverTrail;
    }

    public void setWidthOverTrail(NumberFunction widthOverTrail) {
        this.widthOverTrail = widthOverTrail;
    }

    public TrailParticle.UVMode getUvMode() {
        return uvMode;
    }

    public void setUvMode(TrailParticle.UVMode uvMode) {
        this.uvMode = uvMode;
    }

    public boolean isParallelRendering() {
        return parallelRendering;
    }

    public void setParallelRendering(boolean parallelRendering) {
        this.parallelRendering = parallelRendering;
    }

    public boolean isCalculateSmoothByShader() {
        return calculateSmoothByShader;
    }

    public void setCalculateSmoothByShader(boolean calculateSmoothByShader) {
        this.calculateSmoothByShader = calculateSmoothByShader;
    }

    public boolean isSmoothInterpolation() {
        return smoothInterpolation;
    }

    public void setSmoothInterpolation(boolean smoothInterpolation) {
        this.smoothInterpolation = smoothInterpolation;
    }

    public float getMinVertexDistance() {
        return minVertexDistance;
    }

    public void setMinVertexDistance(float minVertexDistance) {
        this.minVertexDistance = minVertexDistance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getStartDelay() {
        return startDelay;
    }

    public void setStartDelay(int startDelay) {
        this.startDelay = startDelay;
    }

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    protected int duration = 100;

    protected boolean looping = true;

    protected int startDelay = 0;

    protected int time = 20;

    protected float minVertexDistance = 0.05f;

    protected boolean smoothInterpolation = false;

    protected boolean calculateSmoothByShader = false;

    protected boolean parallelRendering = false;

    protected TrailParticle.UVMode uvMode = TrailParticle.UVMode.Stretch;

    protected NumberFunction widthOverTrail = NumberFunction.constant(0.2f);

    protected NumberFunction colorOverTrail = new Gradient();

    public final MaterialSetting material = new MaterialSetting();

    public final RendererSetting renderer = new RendererSetting();

    public final LightOverLifetimeSetting lights = new LightOverLifetimeSetting();

    public final UVAnimationSetting uvAnimation = new UVAnimationSetting();

    public RLParticleRenderType getParticleRenderType() {
        return particleRenderType;
    }

    public UVAnimationSetting getUvAnimation() {
        return uvAnimation;
    }

    public LightOverLifetimeSetting getLights() {
        return lights;
    }

    public RendererSetting getRenderer() {
        return renderer;
    }

    public MaterialSetting getMaterial() {
        return material;
    }

    // runtime
    public final RLParticleRenderType particleRenderType = new RenderType();

    public RLTrailConfig() {
        material.setMaterial(new CustomShaderMaterial());
    }

    private class RenderType extends RLParticleRenderType {
        private BlendMode lastBlend = null;

        @Override
        public void prepareStatus() {
            if (renderer.isBloomEffect()) {
                beginBloom();
            }
            material.pre();
            material.getMaterial().begin(false);
            if (RenderSystem.getShader() instanceof ShaderInstanceAccessor shader) {
                lastBlend = BlendModeAccessor.getLastApplied();
                BlendModeAccessor.setLastApplied(shader.getBlend());
            }
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
        }

        @Override
        public void begin(@Nonnull BufferBuilder bufferBuilder) {
            bufferBuilder.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void releaseStatus() {
            material.getMaterial().end(false);
            material.post();
            if (lastBlend != null) {
                lastBlend.apply();
                lastBlend = null;
            }
            if (renderer.isBloomEffect()) {
                endBloom();
            }
        }

        @Override
        public boolean isParallel() {
            return isParallelRendering();
        }

    }
}
