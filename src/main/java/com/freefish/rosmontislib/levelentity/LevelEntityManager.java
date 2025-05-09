package com.freefish.rosmontislib.levelentity;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.event.packet.toclient.CRClientLevelEntityMessage;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class LevelEntityManager {
    public <T extends LevelEntity> T addLevelEntityToWorld(Level level, LevelEntityType<T> levelEntityType){
        if(level.isClientSide)
            return null;
        else {
            T levelEntity = levelEntityType.createLevelEntity();
            levelEntity.setLevel(level);
            addLevelEntity(levelEntity);
            RosmontisLib.sendMSGToAll(new CRClientLevelEntityMessage(true,levelEntity));
            return levelEntity;
        }
    }

    public abstract LevelEntity getEntityByID(int id);

    public abstract void addLevelEntity(LevelEntity levelEntity);

    public abstract void removeLevelEntity(LevelEntity levelEntity);

    public abstract void tick();

    public abstract List<LevelEntity> levelEntityList();

    public static LevelEntityManager getInstance(Level level){
        return ((ILevelEntityManager) level).getLevelEntityManager();
    }

}
