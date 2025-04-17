package com.freefish.rosmontislib.client.particle.advance.base.beam;

import com.freefish.rosmontislib.client.particle.advance.RLParticleRenderType;
import com.freefish.rosmontislib.client.particle.advance.data.LightOverLifetimeSetting;
import com.freefish.rosmontislib.client.particle.advance.data.MaterialSetting;
import com.freefish.rosmontislib.client.particle.advance.data.RendererSetting;
import com.freefish.rosmontislib.client.particle.advance.data.UVAnimationSetting;
import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction;
import com.freefish.rosmontislib.client.particle.advance.data.number.color.Color;
import com.freefish.rosmontislib.mixin.accessor.BlendModeAccessor;
import com.freefish.rosmontislib.mixin.accessor.ShaderInstanceAccessor;
import com.mojang.blaze3d.shaders.BlendMode;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import org.joml.Vector3f;

import javax.annotation.Nonnull;

public class RLBeamConfig {
    protected int duration = 100;

    protected boolean looping = true;

    protected int startDelay = 0;

    protected Vector3f end = new Vector3f(3, 0, 0);

    protected NumberFunction width = NumberFunction.constant(0.2);

    protected NumberFunction emitRate = NumberFunction.constant(0);

    public NumberFunction getColor() {
        return color;
    }

    public void setColor(NumberFunction color) {
        this.color = color;
    }

    public NumberFunction getEmitRate() {
        return emitRate;
    }

    public void setEmitRate(NumberFunction emitRate) {
        this.emitRate = emitRate;
    }

    public NumberFunction getWidth() {
        return width;
    }

    public void setWidth(NumberFunction width) {
        this.width = width;
    }

    public Vector3f getEnd() {
        return end;
    }

    public void setEnd(Vector3f end) {
        this.end = end;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    protected NumberFunction color = new Color();

    public MaterialSetting getMaterial() {
        return material;
    }

    public RendererSetting getRenderer() {
        return renderer;
    }

    public UVAnimationSetting getUvAnimation() {
        return uvAnimation;
    }

    public LightOverLifetimeSetting getLights() {
        return lights;
    }

    public RLParticleRenderType getParticleRenderType() {
        return particleRenderType;
    }

    public final MaterialSetting material = new MaterialSetting();

    public final RendererSetting renderer = new RendererSetting();

    public final UVAnimationSetting uvAnimation = new UVAnimationSetting();

    public final LightOverLifetimeSetting lights = new LightOverLifetimeSetting();

    // runtime
    public final RLParticleRenderType particleRenderType = new RenderType();

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
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
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
    }
}
