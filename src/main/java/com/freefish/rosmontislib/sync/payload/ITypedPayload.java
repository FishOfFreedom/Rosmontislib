package com.freefish.rosmontislib.sync.payload;

import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nullable;

public interface ITypedPayload<T> {

    byte getType();

    void writePayload(FriendlyByteBuf buf);

    void readPayload(FriendlyByteBuf buf);

    @Nullable
    Tag serializeNBT();

    void deserializeNBT(Tag tag);

    T getPayload();

    boolean isPrimitive();

    default Object copyForManaged(Object value) {
        return value;
    }
}