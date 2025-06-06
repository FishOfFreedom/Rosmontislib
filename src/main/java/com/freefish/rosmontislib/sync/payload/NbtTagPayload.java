/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.sync.payload;

import net.minecraft.nbt.Tag;

public class NbtTagPayload extends ObjectTypedPayload<Tag> {

    public static ITypedPayload<?> of(Tag tag) {
        var payload = new NbtTagPayload();
        payload.setPayload(tag);
        return payload;
    }

    @Override
    public Tag serializeNBT() {
        return payload;
    }

    @Override
    public void deserializeNBT(Tag tag) {
        payload = tag;
    }

    @Override
    public Object copyForManaged(Object value) {
        if (value instanceof Tag) {
            return ((Tag) value).copy();
        }
        return super.copyForManaged(value);
    }
}
