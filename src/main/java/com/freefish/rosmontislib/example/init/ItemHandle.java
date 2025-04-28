package com.freefish.rosmontislib.example.init;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.example.item.ScreenItem;
import com.freefish.rosmontislib.example.item.help.TestItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemHandle {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RosmontisLib.MOD_ID);

    public static final RegistryObject<Item> TEST_ITEM = ITEMS.register("test_item",
            () -> new TestItem(new Item.Properties()));

    public static final RegistryObject<Item> SCREEN_ITEM = ITEMS.register("screen_item",
            () -> new ScreenItem(new Item.Properties()));

    public static final RegistryObject<BlockItem> SHOP = ITEMS.register("entity_spawner_item",
            () -> new BlockItem(BlockHandle.ENTITY_SPAWNER_BLOCK.get(),
                    new Item.Properties()));

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RosmontisLib.MOD_ID);

    public static final RegistryObject<CreativeModeTab> KILLINGFLOOR_TAB = TABS.register("rosmontislib", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + RosmontisLib.MOD_ID + ".rosmontislib"))
            .icon(() -> new ItemStack(ItemHandle.SCREEN_ITEM.get()))
            .displayItems((enabledFeatures, entries) -> {
                for (RegistryObject<Item> item : ItemHandle.ITEMS.getEntries()) {
                    entries.accept(item.get());
                }
            })
            .build());
}
