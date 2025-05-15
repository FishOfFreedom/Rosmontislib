/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.sync.accessor;

import com.freefish.rosmontislib.client.utils.Size;
import com.freefish.rosmontislib.sync.AccessorOp;
import com.freefish.rosmontislib.sync.payload.ITypedPayload;
import com.freefish.rosmontislib.sync.payload.NbtTagPayload;
import net.minecraft.nbt.CompoundTag;

/**
 * @author KilaBash
 * @date 2022/9/7
 * @implNote BlockStateAccessor
 */
public class SizeAccessor extends CustomObjectAccessor<Size>{

    public SizeAccessor() {
        super(Size.class, true);
    }

    @Override
    public ITypedPayload<?> serialize(AccessorOp op, Size value) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("width", value.width);
        tag.putInt("height", value.height);
        return NbtTagPayload.of(tag);
    }

    @Override
    public Size deserialize(AccessorOp op, ITypedPayload<?> payload) {
        if (payload instanceof NbtTagPayload nbtTagPayload) {
            var tag = (CompoundTag)nbtTagPayload.getPayload();
            return new Size(tag.getInt("width"), tag.getInt("height"));
        }
        return null;
    }
}
