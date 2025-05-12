package com.freefish.rosmontislib.example.init;

import com.freefish.rosmontislib.RosmontisLib;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityHandle {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RosmontisLib.MOD_ID);

    //public static final RegistryObject<EntityType<Bullet>> BULLET = ENTITY_TYPE.register("bullet",
    //        () -> EntityType.Builder.<Bullet>of(Bullet::new, MobCategory.MISC).sized(0.2f, 0.2f)
    //                .setUpdateInterval(1).build(new ResourceLocation(RosmontisLib.MOD_ID, "bullet").toString()));
}
