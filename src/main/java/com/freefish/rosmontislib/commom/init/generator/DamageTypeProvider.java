package com.freefish.rosmontislib.commom.init.generator;

import com.freefish.rosmontislib.RosmontisLib;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class DamageTypeProvider extends DamageTypeTagsProvider {
    public DamageTypeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, future, RosmontisLib.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(DamageTypeTags.BYPASSES_INVULNERABILITY).add(RLResourceKey.REAL_DAMAGE);
        this.tag(DamageTypeTags.BYPASSES_RESISTANCE).add(RLResourceKey.REAL_DAMAGE);
        this.tag(DamageTypeTags.ALWAYS_MOST_SIGNIFICANT_FALL).add(RLResourceKey.REAL_DAMAGE);
    }
}
