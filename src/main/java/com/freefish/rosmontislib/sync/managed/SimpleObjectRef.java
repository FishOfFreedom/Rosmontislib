/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.sync.managed;

import com.freefish.rosmontislib.sync.SyncUtils;

class SimpleObjectRef extends ManagedRef {
    private Object oldValue;

    SimpleObjectRef(IManagedVar<?> field) {
        super(field);
        oldValue = getField().value();
    }

    @Override
    public void update() {
        Object newValue = getField().value();
        if ((oldValue == null && newValue != null) || (oldValue != null && newValue == null) || (oldValue != null && SyncUtils.isChanged(oldValue, newValue))) {
            oldValue = getKey().getAccessor().copyForManaged(newValue);
            markAsDirty();
        }
    }
}
