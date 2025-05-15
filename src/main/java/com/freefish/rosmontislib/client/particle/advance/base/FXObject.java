/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.base;

import com.freefish.rosmontislib.client.particle.advance.effect.IEffect;
import com.freefish.rosmontislib.client.utils.Transform;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class FXObject extends Particle implements IFXObject {

    @Nullable
    private Level realLevel;

    protected boolean visible = true;

    public final Transform transform = new Transform();

    protected IEffect effect;

    protected FXObject(ClientLevel level) {
        super(level, 0, 0, 0);
        this.hasPhysics = false;
        this.friction = 1;
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public boolean isVisible() {
        if (!visible) return false;
        //if (transform.parent() != null && transform.parent().sceneObject() instanceof IFXObject ifxObject) {
        //    return ifxObject.isVisible();
        //}
        return true;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void setEffect(IEffect effect) {
        this.effect = effect;
    }

    @Override
    public IEffect getEffect() {
        return effect;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public Level getLevel() {
        return realLevel == null ? super.level : realLevel;
    }

    @Override
    public void setLevel(Level level) {
        this.realLevel = level;
    }

    @Override
    public void move(double x, double y, double z) {
    }

    @Override
    protected int getLightColor(float partialTick) {
        if (this.realLevel == null) {
            return 0;
        }
        var pos = transform.position();
        BlockPos blockPos = BlockPos.containing(pos.x, pos.y, pos.z);
        return this.realLevel.hasChunkAt(blockPos) ? LevelRenderer.getLightColor(this.realLevel, blockPos) : 0;
    }

    @Override
    public void remove(boolean force) {
        remove();
    }

    @Override
    public void tick() {
        updateTick();
    }

    @Override
    public void updateTick() {
        if (effect != null) {
            effect.updateFXObjectTick(this);
        }
    }

    @Override
    public void render(@Nonnull VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        updateFrame(pPartialTicks);
    }

    @Override
    public void updateFrame(float partialTicks) {
        if (effect != null) {
            effect.updateFXObjectFrame(this, partialTicks);
        }
    }

    @Override
    @Nonnull
    public ParticleRenderType getRenderType() {
        return NO_RENDER_RENDER_TYPE;
    }

    // compatibility with forge particle
    public boolean shouldCull() {
        return false;
    }

    public static ParticleRenderType NO_RENDER_RENDER_TYPE = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder builder, TextureManager textureManager) {}

        @Override
        public void end(Tesselator tesselator) {}
    };

    @Override
    public Transform transform() {
        return transform;
    }
}