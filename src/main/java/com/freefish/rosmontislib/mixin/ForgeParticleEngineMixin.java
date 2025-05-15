/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.mixin;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.client.particle.advance.RLParticleRenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public abstract class ForgeParticleEngineMixin{

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;)V",
            at = @At(value = "RETURN"), remap = false)
    private void injectRenderReturn(PoseStack pMatrixStack, MultiBufferSource.BufferSource pBuffer, LightTexture pLightTexture, Camera pActiveRenderInfo, float pPartialTicks, net.minecraft.client.renderer.culling.Frustum clippingHelper, CallbackInfo ci) {
        if(!RosmontisLib.isUsingShaderPack()){
            RLParticleRenderType.finishRender();
        }
    }
}

