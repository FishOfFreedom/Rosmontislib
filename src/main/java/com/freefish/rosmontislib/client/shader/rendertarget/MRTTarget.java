package com.freefish.rosmontislib.client.shader.rendertarget;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

public class MRTTarget extends RenderTarget {
    public IMainTarget screenTarget;

    public MRTTarget(IMainTarget screenTarget) {
        super(false);
        this.screenTarget = screenTarget;
        this.width = ((RenderTarget)screenTarget).width;
        this.height = ((RenderTarget)screenTarget).height;
        this.viewWidth = width;
        this.viewHeight = height;
    }

    @Override
    public int getColorTextureId() {
        return screenTarget.getColorBloomTextureId();
    }

    @Override
    public void resize(int pWidth, int pHeight, boolean pClearError) {
        this.viewWidth = pWidth;
        this.viewHeight = pHeight;
        this.width = pWidth;
        this.height = pHeight;
    }

    @Override
    public void bindRead() {
        RenderSystem.assertOnRenderThread();
        GlStateManager._bindTexture(this.getColorTextureId());
    }

    @Override
    public void bindWrite(boolean pSetViewport) {
    }
}
