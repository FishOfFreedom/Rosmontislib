package com.freefish.rosmontislib.levelentity;

import com.freefish.rosmontislib.event.RLNetworking;
import com.freefish.rosmontislib.event.packet.toclient.CRClientLevelEntityMessage;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class LevelEntityManager {
    public <T extends LevelEntity> T addLevelEntityToWorld(Level level, LevelEntityType<T> levelEntityType){
        if(level.isClientSide)
            return null;
        else {
            T levelEntity = levelEntityType.createLevelEntity();
            addLevelEntity(levelEntity);
            RLNetworking.NETWORK.sendToAll(new CRClientLevelEntityMessage(true,levelEntity));
            return levelEntity;
        }
    }

    public abstract LevelEntity getEntityByID(int id);

    public abstract void addLevelEntity(LevelEntity levelEntity);

    public abstract void removeLevelEntity(LevelEntity levelEntity);

    public abstract void tick();

    public abstract List<LevelEntity> levelEntityList();

    public abstract  <T extends LevelEntity> T getInstanceLevelEntity (InstanceLevelEntityType<T> instanceLevelEntityType);

    public static LevelEntityManager getInstance(Level level){
        return ((ILevelEntityManager) level).getLevelEntityManager();
    }

}
