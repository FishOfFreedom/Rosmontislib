package com.freefish.rosmontislib.levelentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class LevelEntityManagerClient extends LevelEntityManager{
    private List<LevelEntity> levelEntityList = new ArrayList<>();

    @Override
    public LevelEntity getEntityByID(int id) {
        Optional<LevelEntity> first = levelEntityList.stream()
                .filter(levelEntity -> levelEntity.id == id)
                .findFirst();
        return first.orElse(null);
    }

    @Override
    public void addLevelEntity(LevelEntity levelEntity) {
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
}
