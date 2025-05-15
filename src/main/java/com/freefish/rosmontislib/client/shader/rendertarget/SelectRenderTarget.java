/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.shader.rendertarget;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Minecraft;

public class SelectRenderTarget extends RenderTarget {

    public SelectRenderTarget() {
        super(false);
    }

    @Override
    public int getColorTextureId() {
        return  Minecraft.getInstance().getMainRenderTarget().getColorTextureId();
    }
}
