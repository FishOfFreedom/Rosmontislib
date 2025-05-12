package com.freefish.rosmontislib.event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public interface IHandlerContext {
    Object getContext();
    boolean isClient();
    @Nullable ServerPlayer getPlayer();
    @Nullable MinecraftServer getServer();
    Level getLevel();
}
