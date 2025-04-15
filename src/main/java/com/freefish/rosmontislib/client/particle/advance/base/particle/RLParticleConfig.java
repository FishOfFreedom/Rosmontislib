package com.freefish.rosmontislib.client.particle.advance.base.particle;


import com.freefish.rosmontislib.client.particle.advance.RLParticleRenderType;
import com.freefish.rosmontislib.client.particle.advance.data.*;
import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction;
import com.freefish.rosmontislib.client.particle.advance.data.number.NumberFunction3;
import com.freefish.rosmontislib.mixin.accessor.BlendModeAccessor;
import com.freefish.rosmontislib.mixin.accessor.ShaderInstanceAccessor;
import com.mojang.blaze3d.shaders.BlendMode;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

public class RLParticleConfig {
    protected int duration = 100;
//todo
    protected boolean looping = false;

    protected NumberFunction startDelay = NumberFunction.constant(0);

    protected NumberFunction startLifetime = NumberFunction.constant(100);

    protected NumberFunction startSpeed = NumberFunction.constant(1);

    protected NumberFunction3 startSize = new NumberFunction3(0.1, 0.1, 0.1);

    protected NumberFunction3 startRotation = new NumberFunction3(0, 0, 0);

    protected NumberFunction startColor = NumberFunction.color(-1);

    protected int maxParticles = 2000;

    protected EmissionSetting emission = new EmissionSetting();

    protected ShapeSetting shape = new ShapeSetting();

    protected MaterialSetting material = new MaterialSetting();

    protected RendererSetting.Particle renderer = new RendererSetting.Particle();

    protected PhysicsSetting physics = new PhysicsSetting();

    protected LightOverLifetimeSetting lights = new LightOverLifetimeSetting();

    protected final VelocityOverLifetimeSetting velocityOverLifetime = new VelocityOverLifetimeSetting();

    protected final InheritVelocitySetting inheritVelocity = new InheritVelocitySetting();

    protected final LifetimeByEmitterSpeedSetting lifetimeByEmitterSpeed = new LifetimeByEmitterSpeedSetting();

    protected final ForceOverLifetimeSetting forceOverLifetime = new ForceOverLifetimeSetting();

    protected final ColorOverLifetimeSetting colorOverLifetime = new ColorOverLifetimeSetting();

    protected final SizeOverLifetimeSetting sizeOverLifetime = new SizeOverLifetimeSetting();

    protected final RotationOverLifetimeSetting rotationOverLifetime = new RotationOverLifetimeSetting();

    protected final NoiseSetting noise = new NoiseSetting();

    protected final UVAnimationSetting uvAnimation = new UVAnimationSetting();

    public Space getSimulationSpace() {
        return simulationSpace;
    }

    protected Space simulationSpace = Space.Local;

    public LightOverLifetimeSetting getLights() {
        return lights;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isLooping() {
        return looping;
    }

    public NumberFunction getStartDelay() {
        return startDelay;
    }

    public NumberFunction getStartLifetime() {
        return startLifetime;
    }

    public NumberFunction getStartSpeed() {
        return startSpeed;
    }

    public NumberFunction3 getStartSize() {
        return startSize;
    }

    public NumberFunction3 getStartRotation() {
        return startRotation;
    }

    public NumberFunction getStartColor() {
        return startColor;
    }

    public int getMaxParticles() {
        return maxParticles;
    }

    public EmissionSetting getEmission() {
        return emission;
    }

    public ShapeSetting getShape() {
        return shape;
    }

    public RendererSetting getRenderer() {
        return renderer;
    }

    public PhysicsSetting getPhysics() {
        return physics;
    }

    public VelocityOverLifetimeSetting getVelocityOverLifetime() {
        return velocityOverLifetime;
    }

    public InheritVelocitySetting getInheritVelocity() {
        return inheritVelocity;
    }

    public LifetimeByEmitterSpeedSetting getLifetimeByEmitterSpeed() {
        return lifetimeByEmitterSpeed;
    }

    public ForceOverLifetimeSetting getForceOverLifetime() {
        return forceOverLifetime;
    }

    public ColorOverLifetimeSetting getColorOverLifetime() {
        return colorOverLifetime;
    }

    public SizeOverLifetimeSetting getSizeOverLifetime() {
        return sizeOverLifetime;
    }

    public RotationOverLifetimeSetting getRotationOverLifetime() {
        return rotationOverLifetime;
    }

    public NoiseSetting getNoise() {
        return noise;
    }

    public UVAnimationSetting getUvAnimation() {
        return uvAnimation;
    }

    //protected final TrailsSetting trails = new TrailsSetting(this);

    public MaterialSetting getMaterial() {
        return material;
    }


    // runtime
    public final RLParticleRenderType particleRenderType = new RenderType();

    public enum Space {
        Local,
        World
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

        @Override
        public boolean isParallel() {
            return false;
        }
    }
}
