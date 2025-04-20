package com.freefish.rosmontislib.client.particle.advance.data.material;

import com.freefish.rosmontislib.RosmontisLib;
import net.minecraft.resources.ResourceLocation;

public class MaterialHandle {
    public static final TextureMaterial GLOW = new TextureMaterial(new ResourceLocation("textures/particle/glow.png"));
    public static final TextureMaterial VOID = new TextureMaterial(new ResourceLocation(RosmontisLib.MOD_ID,"textures/particle/void.png"));
    public static final TextureMaterial SMOKE = new TextureMaterial(new ResourceLocation(RosmontisLib.MOD_ID,"textures/particle/smoke.png"));
    public static final TextureMaterial RING = new TextureMaterial(new ResourceLocation(RosmontisLib.MOD_ID,"textures/particle/ring.png"));
    public static final CustomShaderMaterial CIRCLE = new CustomShaderMaterial(new ResourceLocation("rosmontislib:circle"));
}

