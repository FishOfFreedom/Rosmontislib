package com.freefish.rosmontislib.levelentity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class InstanceLevelEntityType<T extends LevelEntity> extends LevelEntityType<T> {
    public static final List<InstanceLevelEntityType<?>> instanceList = new ArrayList<>();

    public InstanceLevelEntityType(Supplier<T> factory) {
        super(factory);
        instanceList.add(this);
    }
}
