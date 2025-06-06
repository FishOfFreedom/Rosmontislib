/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.sync.payload;

import com.freefish.rosmontislib.utils.FluidStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public class FluidStackPayload extends ObjectTypedPayload<FluidStack> {

    @Override
    public void writePayload(FriendlyByteBuf buf) {
        payload.writeToBuf(buf);
    }

    @Override
    public void readPayload(FriendlyByteBuf buf) {
        payload = FluidStack.readFromBuf(buf);
    }

    @Override
    public Tag serializeNBT() {
        return payload.saveToTag(new CompoundTag());
    }

    @Override
    public void deserializeNBT(Tag tag) {
        payload = FluidStack.loadFromTag((CompoundTag) tag);
    }

    @Override
    public Object copyForManaged(Object value) {
        if (value instanceof FluidStack stack) {
            return stack.copy();
        }
        return super.copyForManaged(value);
    }
}

