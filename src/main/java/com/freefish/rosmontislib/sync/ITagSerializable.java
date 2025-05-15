/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.sync;

import net.minecraft.nbt.Tag;

public interface ITagSerializable<T extends Tag> {
    T serializeNBT();

    void deserializeNBT(T nbt);
}