/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.sync.accessor;

import com.freefish.rosmontislib.sync.AccessorOp;
import com.freefish.rosmontislib.sync.IAccessor;
import com.freefish.rosmontislib.sync.managed.IManagedVar;
import com.freefish.rosmontislib.sync.managed.IRef;
import com.freefish.rosmontislib.sync.managed.ManagedRef;
import com.freefish.rosmontislib.sync.payload.ITypedPayload;
import com.freefish.rosmontislib.sync.payload.PrimitiveTypedPayload;

public abstract class ManagedAccessor implements IAccessor {
    private byte defaultType = -1;

    @Override
    public byte getDefaultType() {
        return defaultType;
    }

    @Override
    public void setDefaultType(byte defaultType) {
        this.defaultType = defaultType;
    }

    @Override
    public boolean isManaged() {
        return true;
    }

    public abstract ITypedPayload<?> readManagedField(AccessorOp op, IManagedVar<?> field);

    public abstract void writeManagedField(AccessorOp op, IManagedVar<?> field, ITypedPayload<?> payload);

    @Override
    public ITypedPayload<?> readField(AccessorOp op, IRef field) {
        if (!(field instanceof ManagedRef syncedField)) {
            throw new IllegalArgumentException("Field %s is not a managed field".formatted(field));
        }
        var managedField = syncedField.getField();
        if (!managedField.isPrimitive() && managedField.value() == null) {
            return PrimitiveTypedPayload.ofNull();
        }
        return readManagedField(op, managedField);
    }


    @Override
    public void writeField(AccessorOp op, IRef field, ITypedPayload<?> payload) {
        if (!(field instanceof ManagedRef syncedField)) {
            throw new IllegalArgumentException("Field %s is not a managed field".formatted(field));
        }
        var managedField = syncedField.getField();
        writeManagedField(op, managedField, payload);
    }


}