/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.mixin.accessor;

import com.mojang.blaze3d.shaders.BlendMode;
import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.renderer.ShaderInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(ShaderInstance.class)
public interface ShaderInstanceAccessor {
    @Accessor
    BlendMode getBlend();

    @Accessor
    List<String> getSamplerNames();

    @Accessor
    Map<String, Uniform> getUniformMap();
}