package com.freefish.rosmontislib.levelentity;

import com.freefish.rosmontislib.event.RLNetworking;
import com.freefish.rosmontislib.event.packet.toclient.SetLevelEntityDataMessage;
import com.freefish.rosmontislib.levelentity.sync.SynchedLevelEntityData;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;

public class LevelEntity {
    @Getter
    private final LevelEntityType<?> levelEntityType;

    public int tickCount,id;

    @Getter
    @Setter
    private boolean isRemove = false;

    @Nonnull
    @Setter
    public Level level;

    @Getter
    @Setter
    private boolean isInstance = false;

    public SynchedLevelEntityData levelEntityData;

    public LevelEntity(LevelEntityType<?> levelEntityType) {
        this.levelEntityType = levelEntityType;
        this.levelEntityData = new SynchedLevelEntityData(this);
    }

    public void tick(){
        tickCount++;
        if(!level.isClientSide){
            onServerTick();
        }
    }

    public void onServerTick(){
        if(levelEntityData.isDirty()){
            List<SynchedLevelEntityData.DataValue<?>> list = levelEntityData.packDirty();
            if (list != null) {
                RLNetworking.NETWORK.sendToAll(new SetLevelEntityDataMessage(list,id));
            }
        }
    }

    public void onAddFromWorld(){

    }

    public void onRemoveFromWorld(){

    }

    public void onSyncedDataUpdated(List<SynchedLevelEntityData.DataValue<?>> pDataValues) {

    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {

    }

    protected void defineSynchedData(){

    }

    public boolean save(CompoundTag compoundTag) {
        compoundTag.putString("id",LevelEntityHandle.getKey(getLevelEntityType()).toString());
        return true;
    }

    public void load(CompoundTag compoundTag) {
    }
}
