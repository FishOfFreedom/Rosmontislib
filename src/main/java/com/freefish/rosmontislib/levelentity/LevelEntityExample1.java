package com.freefish.rosmontislib.levelentity;

import com.freefish.rosmontislib.levelentity.sync.SynchedLevelEntityData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;

public class LevelEntityExample1 extends LevelEntity {
    protected static final EntityDataAccessor<Integer> TEST = SynchedLevelEntityData.defineId(LevelEntityExample1.class, EntityDataSerializers.INT);

    public int testint;

    public LevelEntityExample1() {
        super(LevelEntityHandle.EXAMPLE_INSTANCE);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void defineSynchedData() {
        this.levelEntityData.define(TEST,1);
    }

    @Override
    public boolean save(CompoundTag compoundTag) {
        return super.save(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
    }
}
