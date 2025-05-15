/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.sync.accessor;

import com.freefish.rosmontislib.sync.AccessorOp;
import com.freefish.rosmontislib.sync.payload.ITypedPayload;
import com.freefish.rosmontislib.sync.payload.NbtTagPayload;
import net.minecraft.nbt.CompoundTag;
import org.joml.Quaternionf;

public class QuaternionfAccessor extends CustomObjectAccessor<Quaternionf>{

    public QuaternionfAccessor() {
        super(Quaternionf.class, true);
    }

    @Override
    public ITypedPayload<?> serialize(AccessorOp op, Quaternionf value) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("x", value.x);
        tag.putFloat("y", value.y);
        tag.putFloat("z", value.z);
        tag.putFloat("w", value.w);
        return NbtTagPayload.of(tag);
    }

    @Override
    public Quaternionf deserialize(AccessorOp op, ITypedPayload<?> payload) {
        if (payload instanceof NbtTagPayload nbtTagPayload) {
            var tag = (CompoundTag)nbtTagPayload.getPayload();
            return new Quaternionf(tag.getFloat("x"), tag.getFloat("y"), tag.getFloat("z"), tag.getFloat("w"));
        }
        return null;
    }
}
