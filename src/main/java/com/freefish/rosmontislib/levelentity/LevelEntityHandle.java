package com.freefish.rosmontislib.levelentity;

import com.freefish.rosmontislib.RosmontisLib;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class LevelEntityHandle {
    protected static final BiMap<ResourceLocation,LevelEntityType<?>> map = HashBiMap.create();

    public static LevelEntityType<LevelEntityExample>          EXAMPLE;
    public static InstanceLevelEntityType<LevelEntityExample1> EXAMPLE_INSTANCE;

    public static void init(){
        EXAMPLE = register(new ResourceLocation(RosmontisLib.MOD_ID,"example"),LevelEntityExample::new);
        EXAMPLE_INSTANCE = registerInstance(new ResourceLocation(RosmontisLib.MOD_ID,"example_instance"),LevelEntityExample1::new);
    }


    public static <T extends LevelEntity> LevelEntityType<T> register(ResourceLocation id, Supplier<T> supplier){
        LevelEntityType<T> levelEntityType = new LevelEntityType<>(supplier);
        map.put(id,levelEntityType);
        return levelEntityType;
    };

    public static <T extends LevelEntity> InstanceLevelEntityType<T> registerInstance(ResourceLocation id, Supplier<T> supplier){
        InstanceLevelEntityType<T> levelEntityType = new InstanceLevelEntityType<>(supplier);
        map.put(id,levelEntityType);
        return levelEntityType;
    };

    public static ResourceLocation  getKey(LevelEntityType<?> levelEntityType){
        if(map.containsValue(levelEntityType)){
            return map.inverse().get(levelEntityType);
        }else {
            RosmontisLib.LOGGER.warn("levelEntityType no register");
            return new ResourceLocation(RosmontisLib.MOD_ID,"example");
        }
    };

    public static LevelEntityType<?> getLevelEntityType(ResourceLocation id){
        if(map.containsKey(id)){
            return map.get(id);
        }else {
            RosmontisLib.LOGGER.warn("levelEntityType resource no register" + id.toString());
            return null;
        }
    };
}
