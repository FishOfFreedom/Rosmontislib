/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.client.particle.advance.data.RendererSetting;
import com.freefish.rosmontislib.client.shader.postprocessing.PostProcessing;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;
import java.util.List;

@OnlyIn(value= Dist.CLIENT)
@ParametersAreNonnullByDefault
public abstract class RLParticleRenderType implements ParticleRenderType {
    @Nullable
    public static Frustum getFRUSTUM() {
        return FRUSTUM;
    }

    public static RendererSetting.Layer getLAYER() {
        return LAYER;
    }

    @Nullable
    private static Frustum FRUSTUM;

    private static RendererSetting.Layer LAYER = RendererSetting.Layer.Translucent;

    public static boolean bloomMark = false;

    public static void prepareForParticleRendering(@Nullable Frustum cullingFrustum) {
        FRUSTUM = cullingFrustum;
        LAYER = RendererSetting.Layer.Opaque;
    }

    public static void renderBloom(){
        if (LAYER == RendererSetting.Layer.Translucent && bloomMark && !RosmontisLib.isUsingShaderPack()) {
            PostProcessing.BLOOM_UNREAL.renderParticlePost();
        }
    }

    public static void finishRender() {
        renderBloom();
        if (LAYER == RendererSetting.Layer.Opaque) {
            LAYER = RendererSetting.Layer.Translucent;
        }
    }

    public void beginBloom() {
        if (!RosmontisLib.isUsingShaderPack()) {
            PostProcessing postProcessing = PostProcessing.BLOOM_UNREAL;
            postProcessing.getPostTarget(false).bindWrite(false);
            bloomMark = true;
        }
    }

    public void endBloom() {
        if (!RosmontisLib.isUsingShaderPack()) {
            var background = Minecraft.getInstance().getMainRenderTarget();
            background.bindWrite(false);
        }
    }

    public boolean isParallel() {
        return false;
    }

    @Override
    @Deprecated
    public final void begin(BufferBuilder builder, TextureManager textureManager) {
        prepareStatus();
        begin(builder);
    }

    @Override
    @Deprecated
    public final void end(Tesselator tesselator) {
        end(tesselator.getBuilder());
        releaseStatus();
    }

    /**
     * setup opengl environment, setup shaders, uniforms.
     */
    public void prepareStatus() {

    }

    /**
     * setup buffer builder, which may be called in async.
     */
    public void begin(BufferBuilder builder) {

    }

    /**
     * upload the buffer builder. In the render thread
     */
    public void end(BufferBuilder builder) {
        BufferUploader.drawWithShader(builder.end());
    }

    /**
     * restore opengl environment.
     */
    public void releaseStatus() {

    }

    /**
     * check is specific layer
     */
    public static boolean checkLayer(RendererSetting.Layer layer) {
        return LAYER == layer;
    }

    /**
     * do cull checking
     */
    public static boolean checkFrustum(AABB aabb) {
        if (FRUSTUM == null) return true;
        return FRUSTUM.isVisible(aabb);
    }

    public static Comparator<ParticleRenderType> makeParticleRenderTypeComparator(List<ParticleRenderType> renderOrder) {
        Comparator<ParticleRenderType> vanillaComparator = Comparator.comparingInt(renderOrder::indexOf);
        return (typeOne, typeTwo) ->
        {
            boolean vanillaOne = renderOrder.contains(typeOne);
            boolean vanillaTwo = renderOrder.contains(typeTwo);

            if (vanillaOne && vanillaTwo)
            {
                return vanillaComparator.compare(typeOne, typeTwo);
            }
            if (!vanillaOne && !vanillaTwo)
            {
                return Integer.compare(System.identityHashCode(typeOne), System.identityHashCode(typeTwo));
            }
            return vanillaOne ? -1 : 1;
        };
    }

}
