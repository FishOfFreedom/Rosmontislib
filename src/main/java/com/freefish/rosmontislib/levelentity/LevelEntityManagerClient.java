package com.freefish.rosmontislib.levelentity;

import net.minecraft.world.level.Level;

import java.util.*;

public class LevelEntityManagerClient extends LevelEntityManager{
    private List<LevelEntity> levelEntityList = new ArrayList<>();
    private final Level level;
    public final Map<InstanceLevelEntityType<?>,LevelEntity> instanceLevelEntityTypeLevelEntityMap = new HashMap<>();

    public LevelEntityManagerClient(Level level) {
        this.level = level;
    }

    @Override
    public LevelEntity getEntityByID(int id) {
        Optional<LevelEntity> first = levelEntityList.stream()
                .filter(levelEntity -> levelEntity.id == id)
                .findFirst();
        return first.orElse(null);
    }

    @Override
    public void addLevelEntity(LevelEntity levelEntity) {
        levelEntity.setLevel(level);
        levelEntity.defineSynchedData();
        levelEntityList.add(levelEntity);
        levelEntity.onAddFromWorld();
    }

    @Override
    public void removeLevelEntity(LevelEntity levelEntity) {
        levelEntityList.remove(levelEntity);
    }

    @Override
    public void tick() {
        Iterator<LevelEntity> iterator = levelEntityList.iterator();
        while (iterator.hasNext()){
            LevelEntity next = iterator.next();
            next.tick();
        }
    }

    @Override
    public List<LevelEntity> levelEntityList() {
        return levelEntityList;
    }

    @Override
    public <T extends LevelEntity> T getInstanceLevelEntity(InstanceLevelEntityType<T> instanceLevelEntityType) {
        return (T) instanceLevelEntityTypeLevelEntityMap.computeIfAbsent(instanceLevelEntityType,(instanceLevelEntityTypeMap -> null));
    }
}
