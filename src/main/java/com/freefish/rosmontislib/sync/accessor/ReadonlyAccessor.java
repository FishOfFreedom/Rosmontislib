/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.sync.accessor;

import com.freefish.rosmontislib.sync.AccessorOp;
import com.freefish.rosmontislib.sync.IAccessor;
import com.freefish.rosmontislib.sync.TypedPayloadRegistries;
import com.freefish.rosmontislib.sync.managed.IRef;
import com.freefish.rosmontislib.sync.managed.ReadOnlyManagedRef;
import com.freefish.rosmontislib.sync.payload.ITypedPayload;
import com.freefish.rosmontislib.sync.payload.NbtTagPayload;
import com.freefish.rosmontislib.sync.payload.PrimitiveTypedPayload;
import net.minecraft.nbt.CompoundTag;

public abstract class ReadonlyAccessor implements IAccessor {

    private byte defaultType = -1;

    @Override
    public byte getDefaultType() {
        return defaultType;
    }

    @Override
    public void setDefaultType(byte defaultType) {
        this.defaultType = defaultType;
    }

    public abstract ITypedPayload<?> readFromReadonlyField(AccessorOp op, Object obj);

    public abstract void writeToReadonlyField(AccessorOp op, Object obj, ITypedPayload<?> payload);

    @Override
    public boolean isManaged() {
        return false;
    }

    @Override
    public ITypedPayload<?> readField(AccessorOp op, IRef field) {
        var obj = field.readRaw();
        if (field instanceof ReadOnlyManagedRef managedRef) {
            if (obj == null) {
                return PrimitiveTypedPayload.NullPayload.ofNull();
            } else {
                var tag = new CompoundTag();
                tag.put("uid", managedRef.getReadOnlyField().serializeUid(obj));

                var payloadTag = new CompoundTag();
                var payload = readFromReadonlyField(op, obj);
                payloadTag.putByte("t", payload.getType());
                var data = payload.serializeNBT();
                if (data != null) {
                    payloadTag.put("d", data);
                }
                tag.put("payload", payloadTag);
                return NbtTagPayload.of(tag);
            }
        } else if (obj == null) {
            throw new IllegalArgumentException("readonly field %s has a null reference".formatted(field.getKey()));
        }
        return readFromReadonlyField(op, obj);
    }

    @Override
    public void writeField(AccessorOp op, IRef field, ITypedPayload<?> payload) {
        var obj = field.readRaw();
        if (field instanceof ReadOnlyManagedRef managedRef) {
            var readOnlyField = managedRef.getReadOnlyField();
            if(payload instanceof PrimitiveTypedPayload<?> primitive && primitive.isNull()) {
                readOnlyField.set(null);
            } else if (payload instanceof NbtTagPayload nbtTagPayload && nbtTagPayload.getPayload() instanceof CompoundTag tag) {
                var uid = tag.getCompound("uid");
                if (obj == null || !readOnlyField.serializeUid(obj).equals(uid)) { // need to update obj
                    obj = managedRef.getReadOnlyField().deserializeUid(uid);
                    readOnlyField.set(obj);
                }
                var payloadTag = tag.getCompound("payload");
                byte id = payloadTag.getByte("t");
                var p = TypedPayloadRegistries.create(id);
                p.deserializeNBT(payloadTag.get("d"));
                writeToReadonlyField(op, obj, p);
            }
            return;
        } else if (obj == null) {
            throw new IllegalArgumentException("readonly field %s has a null reference".formatted(field));
        }
        writeToReadonlyField(op, obj, payload);
    }

}
