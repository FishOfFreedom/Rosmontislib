package com.freefish.rosmontislib.levelentity;

public class LevelEntityExample extends LevelEntity {
    @Override
    public void tick() {
        super.tick();

        if(tickCount>=100){
            setRemove(true);
        }
        System.out.println(tickCount);
    }
}
