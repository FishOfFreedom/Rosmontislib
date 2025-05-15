/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.sync.accessor;

import com.freefish.rosmontislib.sync.AccessorOp;
import com.freefish.rosmontislib.sync.payload.ITypedPayload;
import com.freefish.rosmontislib.sync.payload.NbtTagPayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;

public class AABBAccessor extends CustomObjectAccessor<AABB>{

    public AABBAccessor() {
        super(AABB.class, true);
    }

    @Override
    public ITypedPayload<?> serialize(AccessorOp op, AABB value) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("minX", value.minX);
        tag.putDouble("minY", value.minY);
        tag.putDouble("minZ", value.minZ);
        tag.putDouble("maxX", value.maxX);
        tag.putDouble("maxY", value.maxY);
        tag.putDouble("maxZ", value.maxZ);
        return NbtTagPayload.of(tag);
    }

    @Override
    public AABB deserialize(AccessorOp op, ITypedPayload<?> payload) {
        if (payload instanceof NbtTagPayload nbtTagPayload && nbtTagPayload.getPayload() instanceof CompoundTag tag) {
            return new AABB(
                    tag.getDouble("minX"),
                    tag.getDouble("minY"),
                    tag.getDouble("minZ"),
                    tag.getDouble("maxX"),
                    tag.getDouble("maxY"),
                    tag.getDouble("maxZ")
            );
        }
        return null;
    }
}
