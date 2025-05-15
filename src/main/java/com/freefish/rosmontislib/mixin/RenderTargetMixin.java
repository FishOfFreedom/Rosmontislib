/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.mixin;

import com.freefish.rosmontislib.client.shader.rendertarget.IMainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderTarget.class)
public abstract class RenderTargetMixin {

    @Redirect(method = "createBuffers",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;setFilterMode(I)V"))
    private void injectCreateBuffers(RenderTarget instance, int pFilterMode) {
        instance.setFilterMode(instance.filterMode == 0 ? pFilterMode : instance.filterMode);
    }

    @Inject(method = "destroyBuffers", at = @At("TAIL"))
    private void injectDestroyBuffers(CallbackInfo ci) {
        if (this instanceof IMainTarget mainTarget) {
            mainTarget.destroyBloomTextureBuffers();
        }
    }

    @Inject(method = "setFilterMode", at = @At("TAIL"))
    private void injectSetFilterMode(int $$0, CallbackInfo ci) {
        if (this instanceof IMainTarget mainTarget) {
            mainTarget.setBloomFilterMode($$0);
        }
    }

    @Inject(method = "createBuffers", at = @At("HEAD"))
    private void injectCreateBuffersHead(int pWidth, int pHeight, boolean pClearError, CallbackInfo ci) {
        if (this instanceof IMainTarget mainTarget) {
            mainTarget.createBuffersHeads(pWidth, pHeight, pClearError);
        }
    }

    @Inject(method = "createBuffers", at = @At("TAIL"))
    private void injectCreateBuffersTail(int pWidth, int pHeight, boolean pClearError, CallbackInfo ci) {
        if (this instanceof IMainTarget mainTarget) {
            mainTarget.createBuffersTail(pWidth, pHeight, pClearError);
        }
    }
}
