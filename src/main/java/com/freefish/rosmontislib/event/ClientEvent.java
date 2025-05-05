package com.freefish.rosmontislib.event;

import com.freefish.rosmontislib.RosmontisLib;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RosmontisLib.MOD_ID, value = Dist.CLIENT)
public class ClientEvent {
    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if(player == null || !minecraft.isWindowActive())
            return;

        //todo skip
        if (event.getOverlay().id() == VanillaGuiOverlay.HOTBAR.id()) {
            //RGuiHandle.INSTANCE.renderHUD(event.getGuiGraphics(),event.getPartialTick());
        }
    }
}
