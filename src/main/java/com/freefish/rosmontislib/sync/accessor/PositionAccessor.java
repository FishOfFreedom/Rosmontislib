/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.sync.accessor;

import com.freefish.rosmontislib.client.utils.Position;
import com.freefish.rosmontislib.sync.AccessorOp;
import com.freefish.rosmontislib.sync.payload.ITypedPayload;
import com.freefish.rosmontislib.sync.payload.NbtTagPayload;
import net.minecraft.nbt.CompoundTag;

/**
 * @author KilaBash
 * @date 2022/9/7
 * @implNote BlockStateAccessor
 */
public class PositionAccessor extends CustomObjectAccessor<Position>{

    public PositionAccessor() {
        super(Position.class, true);
    }

    @Override
    public ITypedPayload<?> serialize(AccessorOp op, Position value) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", value.x);
        tag.putInt("y", value.y);
        return NbtTagPayload.of(tag);
    }

    @Override
    public Position deserialize(AccessorOp op, ITypedPayload<?> payload) {
        if (payload instanceof NbtTagPayload nbtTagPayload) {
            var tag = (CompoundTag)nbtTagPayload.getPayload();
            return new Position(tag.getInt("x"), tag.getInt("y"));
        }
        return null;
    }
}
