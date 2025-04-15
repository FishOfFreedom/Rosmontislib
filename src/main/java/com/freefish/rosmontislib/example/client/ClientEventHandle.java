package com.freefish.rosmontislib.example.client;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.example.gui.screen.EntitySpawnerScreen;
import com.freefish.rosmontislib.example.init.MenuHandle;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RosmontisLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandle {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event){

        MenuScreens.register(MenuHandle.ENTITY_SPAWNER_MENU.get(), EntitySpawnerScreen::new);
    }
}
