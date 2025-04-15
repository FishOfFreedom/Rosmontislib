package com.freefish.rosmontislib.mixin;

import com.freefish.rosmontislib.client.shader.postprocessing.PostProcessing;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Inject(method = "resize", at = @At(value = "RETURN"))
    private void injectResize(int width, int height, CallbackInfo ci) {
        PostProcessing.resize(width, height);
    }
}
