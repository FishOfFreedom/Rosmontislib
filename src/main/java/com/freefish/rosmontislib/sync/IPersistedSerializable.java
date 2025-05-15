/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.sync;

import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;

public interface IPersistedSerializable extends ITagSerializable<CompoundTag> {

    @Override
    default CompoundTag serializeNBT() {
        var tag = new CompoundTag();
        PersistedParser.serializeNBT(tag, this.getClass(), this);
        return tag;
    }

    @Override
    default void deserializeNBT(CompoundTag tag) {
        PersistedParser.deserializeNBT(tag, new HashMap<>(), this.getClass(), this);
    }

}