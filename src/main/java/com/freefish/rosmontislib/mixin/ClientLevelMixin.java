package com.freefish.rosmontislib.mixin;

import com.freefish.rosmontislib.levelentity.ILevelEntityManager;
import com.freefish.rosmontislib.levelentity.LevelEntityManagerClient;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(value = ClientLevel.class)
public abstract class ClientLevelMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void initLevelEntity(ClientPacketListener pConnection, ClientLevel.ClientLevelData pClientLevelData, ResourceKey<Level> pDimension,
                                 Holder<DimensionType> pDimensionType, int pViewDistance, int pServerSimulationDistance,
                                 Supplier<ProfilerFiller> pProfiler, LevelRenderer pLevelRenderer, boolean pIsDebug, long pBiomeZoomSeed, CallbackInfo ci) {
        ((ILevelEntityManager) this).setLevelEntityManager(new LevelEntityManagerClient());
    }
}