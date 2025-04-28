package com.freefish.rosmontislib.event;

import com.freefish.rosmontislib.client.ClientHandle;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
}
