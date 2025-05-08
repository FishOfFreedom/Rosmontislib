package com.freefish.rosmontislib.levelentity;

import java.util.function.Supplier;

public class LevelEntityType<T extends LevelEntity> {
    public LevelEntityType(Supplier<T> factory) {
        this.factory = factory;
    }

    private final Supplier<T> factory;

    public T createLevelEntity(){
        return factory.get();
    }
}
