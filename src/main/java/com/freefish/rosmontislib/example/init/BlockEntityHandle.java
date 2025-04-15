package com.freefish.rosmontislib.example.init;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.example.block.blockentity.EntitySpawnerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityHandle {
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister
            .create(ForgeRegistries.BLOCK_ENTITY_TYPES, RosmontisLib.MOD_ID);

    public static final RegistryObject<BlockEntityType<EntitySpawnerBlockEntity>> ENTITY_SPAWNER_ENTITY = TILES
            .register("entity_spawner", () -> BlockEntityType.Builder
                    .of(EntitySpawnerBlockEntity::new, BlockHandle.ENTITY_SPAWNER_BLOCK.get()).build(null));
}
