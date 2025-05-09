package com.freefish.rosmontislib.levelentity.sync;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

public class SavedLevelEntityData extends SavedData {
    private static final String DATA_NAME = "rlsaved_level_entity_data";

    public static SavedLevelEntityData get(ServerLevel world) {
        DimensionDataStorage data = world.getDataStorage();
        SavedLevelEntityData lunarData = data.computeIfAbsent(SavedLevelEntityData::load, SavedLevelEntityData::new, DATA_NAME);
        return lunarData;
    }

    private CompoundTag levelEntityManagerTag;

    public SavedLevelEntityData() {
        this.levelEntityManagerTag = new CompoundTag();
    }

    public SavedLevelEntityData(@NotNull CompoundTag levelEntityManager) {
        this.levelEntityManagerTag = levelEntityManager;
    }

    public static SavedLevelEntityData load(CompoundTag nbt) {
        return new SavedLevelEntityData(nbt);
    }

    @Override
    public CompoundTag save(@NotNull CompoundTag compound) {
        return levelEntityManagerTag;
    }

    public CompoundTag getLevelEntityManager() {
        return levelEntityManagerTag;
    }

    public void setLevelEntityManager(CompoundTag levelEntityManager) {
        this.levelEntityManagerTag = levelEntityManager;
        setDirty();
    }
}
