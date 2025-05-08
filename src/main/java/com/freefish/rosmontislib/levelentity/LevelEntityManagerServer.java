package com.freefish.rosmontislib.levelentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LevelEntityManagerServer extends LevelEntityManager{
    private List<LevelEntity> levelEntityList = new ArrayList<>();

    @Override
    void addLevelEntity(LevelEntity levelEntity) {
        levelEntityList.add(levelEntity);
        levelEntity.onAddFromWorld();
    }

    @Override
    public void tick() {
        Iterator<LevelEntity> iterator = levelEntityList.iterator();
        while (iterator.hasNext()){
            LevelEntity next = iterator.next();
            next.tick();
            if (next.isRemove()) {
                next.onRemoveFromWorld();
                iterator.remove();
            }
        }
    }

    @Override
    public List<LevelEntity> levelEntityList() {
        return levelEntityList;
    }
}
