package com.freefish.rosmontislib.mixin.accessor;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayer.class)
public interface ServerPlayerAccessor {
    @Invoker void callNextContainerCounter();
    @Invoker void callInitMenu(AbstractContainerMenu container);
    @Accessor int getContainerCounter();

}
