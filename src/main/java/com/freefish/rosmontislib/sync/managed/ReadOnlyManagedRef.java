/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.sync.managed;

import net.minecraft.nbt.CompoundTag;

public class ReadOnlyManagedRef extends ManagedRef {

    private boolean wasNull;
    private CompoundTag lastUid;

    ReadOnlyManagedRef(ReadOnlyManagedField field) {
        super(field);
        var current = getField().value();
        wasNull = current == null;
        if (current != null) {
            lastUid = getReadOnlyField().serializeUid(current);
        }
    }

    public ReadOnlyManagedField getReadOnlyField() {
        return ((ReadOnlyManagedField)field);
    }

    @Override
    public void update() {
        Object newValue = getField().value();
        if ((wasNull && newValue != null) || (!wasNull && newValue == null)) {
            markAsDirty();
        }
        wasNull = newValue == null;
        if (newValue != null) {
            var newUid = getReadOnlyField().serializeUid(newValue);
            if (!newUid.equals(lastUid) || getReadOnlyField().isDirty(newValue)) {
                markAsDirty();
            }
            lastUid = newUid;
        }
    }
}