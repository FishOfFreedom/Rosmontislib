package com.freefish.rosmontislib.mixin;

import com.freefish.rosmontislib.levelentity.ILevelEntityManager;
import com.freefish.rosmontislib.levelentity.LevelEntityManager;
import com.freefish.rosmontislib.levelentity.LevelEntityManagerServer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;

@Mixin(value = ServerLevel.class)
public abstract class ServerLevelMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void initLevelEntity(MinecraftServer pServer, Executor pDispatcher, LevelStorageSource.LevelStorageAccess pLevelStorageAccess,
                                 ServerLevelData pServerLevelData, ResourceKey<Level> pDimension, LevelStem pLevelStem,
                                 ChunkProgressListener pProgressListener, boolean pIsDebug, long pBiomeZoomSeed,
                                 List<CustomSpawner> pCustomSpawners, boolean pTickTime, RandomSequences pRandomSequences, CallbackInfo ci) {
        ((ILevelEntityManager) this).setLevelEntityManager(new LevelEntityManagerServer());
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void tickLevelEntity(BooleanSupplier pHasTimeLeft, CallbackInfo ci) {
        LevelEntityManager levelEntityManager = ((ILevelEntityManager) this).getLevelEntityManager();
        if(levelEntityManager!=null){
            levelEntityManager.tick();
        }
    }
}