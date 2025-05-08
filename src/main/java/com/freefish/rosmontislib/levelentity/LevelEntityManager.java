package com.freefish.rosmontislib.levelentity;

import net.minecraft.world.level.Level;

import java.util.List;

public abstract class LevelEntityManager {
    public <T extends LevelEntity> T addLevelEntityToWorld(Level level, LevelEntityType<T> levelEntityType){
        if(level.isClientSide)
            return null;
        else {
            T levelEntity = levelEntityType.createLevelEntity();
            addLevelEntity(levelEntity);
            return levelEntity;
        }
    }

    abstract void addLevelEntity(LevelEntity levelEntity);

    public abstract void tick();

    public abstract List<LevelEntity> levelEntityList();

    public static LevelEntityManager getInstance(Level level){
        return ((ILevelEntityManager) level).getLevelEntityManager();
    }

}
