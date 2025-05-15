/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.sync.accessor;

import com.freefish.rosmontislib.sync.AccessorOp;
import com.freefish.rosmontislib.sync.managed.IManagedVar;
import com.freefish.rosmontislib.sync.payload.ITypedPayload;
import com.freefish.rosmontislib.sync.payload.ObjectTypedPayload;
import com.freefish.rosmontislib.sync.payload.PrimitiveTypedPayload;

public abstract class SimpleObjectAccessor extends ManagedAccessor {

    private final Class<?>[] operandTypes;

    protected SimpleObjectAccessor(Class<?>... operandTypes) {
        this.operandTypes = operandTypes;
    }

    @Override
    public Class<?>[] operandTypes() {
        return operandTypes;
    }

    public abstract ObjectTypedPayload<?> createEmpty();

    @Override
    public ITypedPayload<?> readManagedField(AccessorOp op, IManagedVar<?> field) {
        var value = field.value();
        if (value != null) {
            //noinspection unchecked
            return ((ObjectTypedPayload)createEmpty()).setPayload(value);
        }
        return PrimitiveTypedPayload.ofNull();
    }

    @Override
    public void writeManagedField(AccessorOp op, IManagedVar<?> field, ITypedPayload<?> payload) {
        if (payload instanceof ObjectTypedPayload<?> object) {
            //noinspection unchecked
            ((IManagedVar<Object>) field).set(object.getPayload());
        }
        if(payload instanceof PrimitiveTypedPayload<?> primitive && primitive.isNull()) {
            field.set(null);
        }
    }

}