package com.freefish.rosmontislib.levelentity;

import com.freefish.rosmontislib.RosmontisLib;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class LevelEntityType<T extends LevelEntity> {
    public LevelEntityType(Supplier<T> factory) {
        this.factory = factory;
    }

    private final Supplier<T> factory;

    public T createLevelEntity(){
        return factory.get();
    }

    public static Stream<LevelEntity> loadEntitiesRecursive(final List<? extends Tag> pTags, final Level pLevel) {
        final Spliterator<? extends Tag> spliterator = pTags.spliterator();
        return StreamSupport.stream(new Spliterator<LevelEntity>() {
            public boolean tryAdvance(Consumer<? super LevelEntity> p_147066_) {
                return spliterator.tryAdvance((p_147059_) -> {
                    LevelEntityType.loadEntityRecursive((CompoundTag)p_147059_, pLevel);
                });
            }

            public Spliterator<LevelEntity> trySplit() {
                return null;
            }

            public long estimateSize() {
                return (long)pTags.size();
            }

            public int characteristics() {
                return 1297;
            }
        }, false);
    }

    public static LevelEntity loadEntityRecursive(final CompoundTag compoundTag, final Level pLevel) {
        String id = compoundTag.getString("id");
        ResourceLocation resourceLocation = new ResourceLocation(RosmontisLib.MOD_ID, id);

        LevelEntityType<?> levelEntityType = LevelEntityHandle.getLevelEntityType(resourceLocation);
        LevelEntity levelEntity = levelEntityType.createLevelEntity();
        levelEntity.setLevel(pLevel);
        return levelEntity;
    }
}
