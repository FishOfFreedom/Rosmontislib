package com.freefish.rosmontislib.commom.init.generator;


import com.freefish.rosmontislib.RosmontisLib;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class RLResourceKey {
    public static final ResourceKey<DamageType> REAL_DAMAGE = registerResourceKey(Registries.DAMAGE_TYPE, "real_damage");

    private static <T> ResourceKey<T> registerResourceKey(ResourceKey<Registry<T>> registry, String key) {
        return ResourceKey.create(registry, new ResourceLocation(RosmontisLib.MOD_ID, key));
    }
}
