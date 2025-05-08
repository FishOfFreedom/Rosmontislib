package com.freefish.rosmontislib.levelentity;

import com.freefish.rosmontislib.RosmontisLib;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class LevelEntityHandle {
    protected static final Map<ResourceLocation,LevelEntityType<?>> map = new HashMap<>();

    public static final LevelEntityType<LevelEntityExample> EXAMPLE = register(new ResourceLocation(RosmontisLib.MOD_ID,"example"),LevelEntityExample::new);

    public static <T extends LevelEntity> LevelEntityType<T> register(ResourceLocation id, Supplier<T> supplier){
        LevelEntityType<T> levelEntityType = new LevelEntityType<>(supplier);
        map.put(id,levelEntityType);
        return levelEntityType;
    };
}
