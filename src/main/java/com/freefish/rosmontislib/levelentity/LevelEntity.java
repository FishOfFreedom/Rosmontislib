package com.freefish.rosmontislib.levelentity;

import lombok.Getter;
import lombok.Setter;

public class LevelEntity {
    public int tickCount;

    @Getter
    @Setter
    private boolean isRemove = false;

    public void tick(){
        tickCount++;
    }

    public void onAddFromWorld(){

    }

    public void onRemoveFromWorld(){

    }
}
