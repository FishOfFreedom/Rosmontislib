package com.freefish.rosmontislib.client.particle.advance.data.material;

import com.freefish.rosmontislib.RosmontisLib;
import net.minecraft.resources.ResourceLocation;

public class MaterialHandle {
    public static MaterialType<TextureMaterial> GLOW;
    public static MaterialType<TextureMaterial> VOID;
    public static MaterialType<TextureMaterial> SMOKE;
    public static MaterialType<TextureMaterial> RING;
    public static MaterialType<CustomShaderMaterial> CIRCLE;

    public static void init(){
        GLOW = new MaterialType<>(() -> new TextureMaterial(new ResourceLocation("textures/particle/glow.png")));
        VOID = new MaterialType<>(() -> new TextureMaterial(new ResourceLocation(RosmontisLib.MOD_ID,"textures/particle/void.png")));
        SMOKE = new MaterialType<>(() -> new TextureMaterial(new ResourceLocation(RosmontisLib.MOD_ID,"textures/particle/smoke.png")));
        RING = new MaterialType<>(() -> new TextureMaterial(new ResourceLocation(RosmontisLib.MOD_ID,"textures/particle/ring.png")));
        CIRCLE = new MaterialType<>(() -> new CustomShaderMaterial(new ResourceLocation("rosmontislib:circle")));
    }
}

