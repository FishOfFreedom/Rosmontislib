/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.sync.accessor;

import com.freefish.rosmontislib.sync.AccessorOp;
import com.freefish.rosmontislib.sync.payload.ITypedPayload;
import com.freefish.rosmontislib.sync.payload.NbtTagPayload;
import net.minecraft.nbt.CompoundTag;
import org.joml.Vector3f;

/**
 * @author KilaBash
 * @date 2022/9/7
 * @implNote Vector3Accessor
 */
public class Vector3fAccessor extends CustomObjectAccessor<Vector3f>{

    public Vector3fAccessor() {
        super(Vector3f.class, true);
    }

    @Override
    public ITypedPayload<?> serialize(AccessorOp op, Vector3f value) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("x", value.x);
        tag.putFloat("y", value.y);
        tag.putFloat("z", value.z);
        return NbtTagPayload.of(tag);
    }

    @Override
    public Vector3f deserialize(AccessorOp op, ITypedPayload<?> payload) {
        if (payload instanceof NbtTagPayload nbtTagPayload) {
            var tag = (CompoundTag)nbtTagPayload.getPayload();
            return new Vector3f(tag.getFloat("x"), tag.getFloat("y"), tag.getFloat("z"));
        }
        return null;
    }
}
