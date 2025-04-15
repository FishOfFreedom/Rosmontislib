package com.freefish.rosmontislib.example.init;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.example.block.EntitySpawnerBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockHandle {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, RosmontisLib.MOD_ID);

    public static final RegistryObject<Block> ENTITY_SPAWNER_BLOCK = BLOCKS.register("entity_spawner_block", EntitySpawnerBlock::new);

    private static <T extends Block> RegistryObject<T> registryBlock(String name, Supplier<T> block, boolean isEntity) {
        RegistryObject<T> register = BLOCKS.register(name, block);
        registerBlockItem(name, register, new Item.Properties(), isEntity);
        return register;
    }

    private static <T extends Block> RegistryObject<T> registryBlock(String name, Supplier<T> block, boolean isEntity, Item.Properties properties) {
        RegistryObject<T> register = BLOCKS.register(name, block);
        registerBlockItem(name, register, properties, isEntity);
        return register;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block, Item.Properties properties, boolean isEntityBlock) {
        ItemHandle.ITEMS.register(name, () -> new BlockItem(block.get(), properties));
    }
}
