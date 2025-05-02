package com.freefish.rosmontislib.event;

import com.freefish.rosmontislib.client.ClientHandle;
import com.freefish.rosmontislib.item.ItemAdditionData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public final class CommonEvent {
    @SubscribeEvent
    public void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Level level = event.level;
            if(level.isClientSide){
                ClientHandle.INSTANCE.tick();
            }
        }
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        if (item  instanceof ItemAdditionData<?> itemAdditionData) {
            itemAdditionData.addAdditionTextTool(event);
        }
    }
}
