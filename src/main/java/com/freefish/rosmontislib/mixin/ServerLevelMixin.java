package com.freefish.rosmontislib.mixin;

import com.freefish.rosmontislib.levelentity.ILevelEntityManager;
import com.freefish.rosmontislib.levelentity.LevelEntityManager;
import com.freefish.rosmontislib.levelentity.LevelEntityManagerServer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.ProgressListener;
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

import javax.annotation.Nullable;
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

        ((ILevelEntityManager) this).setLevelEntityManager(new LevelEntityManagerServer((ServerLevel)(Object)this));
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void tickLevelEntity(BooleanSupplier pHasTimeLeft, CallbackInfo ci) {
        LevelEntityManager levelEntityManager = ((ILevelEntityManager) this).getLevelEntityManager();
        if(levelEntityManager!=null){
            levelEntityManager.tick();
        }
    }

    @Inject(method = "save", at = @At("RETURN"))
    public void save(@Nullable ProgressListener pProgress, boolean pFlush, boolean pSkipSave, CallbackInfo ci) {
        if (!pSkipSave) {
            LevelEntityManagerServer levelEntityManager = (LevelEntityManagerServer) ((ILevelEntityManager) this).getLevelEntityManager();
            levelEntityManager.serializeNBT();
        }
    }
}