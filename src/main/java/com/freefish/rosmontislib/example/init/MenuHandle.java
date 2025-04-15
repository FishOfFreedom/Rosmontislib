package com.freefish.rosmontislib.example.init;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.example.gui.screen.EntitySpawnerMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuHandle {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, RosmontisLib.MOD_ID);

    public static final RegistryObject<MenuType<EntitySpawnerMenu>> ENTITY_SPAWNER_MENU = registerMenuType("entity_spawner_menu", EntitySpawnerMenu::new);

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }
}
