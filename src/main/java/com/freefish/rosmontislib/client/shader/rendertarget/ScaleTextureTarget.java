/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.shader.rendertarget;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;

public class ScaleTextureTarget extends RenderTarget {
    float scaleWidth;
    float scaleHeight;

    public ScaleTextureTarget(float scaleWidth, float scaleHeight, int pWidth, int pHeight, boolean pUseDepth, boolean pClearError) {
        super(pUseDepth);
        this.scaleWidth = scaleWidth;
        this.scaleHeight = scaleHeight;
        RenderSystem.assertOnRenderThreadOrInit();
        this.resize(pWidth, pHeight, pClearError);
    }

    @Override
    public void resize(int pWidth, int pHeight, boolean pClearError) {
        super.resize((int)(pWidth * scaleWidth), (int)(pHeight * scaleHeight), pClearError);
    }

}
