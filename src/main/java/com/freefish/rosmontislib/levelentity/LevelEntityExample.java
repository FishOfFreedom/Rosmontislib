package com.freefish.rosmontislib.levelentity;

import com.freefish.rosmontislib.levelentity.sync.SynchedLevelEntityData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;

public class LevelEntityExample extends LevelEntity {
    protected static final EntityDataAccessor<Integer> TEST = SynchedLevelEntityData.defineId(LevelEntityExample.class, EntityDataSerializers.INT);

    public LevelEntityExample() {
        super(LevelEntityHandle.EXAMPLE);
    }

    @Override
    public void tick() {
        if(!level.isClientSide&&tickCount==20){
            this.levelEntityData.set(TEST,10);
        }

        if(tickCount>=100){
            setRemove(true);
        }

        super.tick();
    }

    @Override
    protected void defineSynchedData() {
        this.levelEntityData.define(TEST,1);
    }
}
