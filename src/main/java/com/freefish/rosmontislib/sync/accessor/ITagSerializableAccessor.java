/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.sync.accessor;

import com.freefish.rosmontislib.sync.AccessorOp;
import com.freefish.rosmontislib.sync.ITagSerializable;
import com.freefish.rosmontislib.sync.payload.ITypedPayload;
import com.freefish.rosmontislib.sync.payload.NbtTagPayload;
import net.minecraft.nbt.Tag;

public class ITagSerializableAccessor extends ReadonlyAccessor {
    @Override
    public boolean hasPredicate() {
        return true;
    }

    @Override
    public boolean test(Class<?> type) {
        return ITagSerializable.class.isAssignableFrom(type);
    }

    @Override
    public ITypedPayload<?> readFromReadonlyField(AccessorOp op, Object obj) {
        if(!(obj instanceof ITagSerializable<?> serializable)) {
            throw new IllegalArgumentException("Field %s is not ITagSerializable".formatted(obj));
        }

        var nbt = serializable.serializeNBT();

        return new NbtTagPayload().setPayload(nbt);
    }

    @Override
    public void writeToReadonlyField(AccessorOp op, Object obj, ITypedPayload<?> payload) {
        if(!(obj instanceof ITagSerializable<?>)) {
            throw new IllegalArgumentException("Field %s is not ITagSerializable".formatted(obj));
        }

        if(!(payload instanceof NbtTagPayload nbtPayload)) {
            throw new IllegalArgumentException("Payload %s is not NbtTagPayload".formatted(payload));
        }

        //noinspection unchecked
        ((ITagSerializable<Tag>) obj).deserializeNBT(nbtPayload.getPayload());
    }
}