/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.sync.managed;

import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * @author KilaBash
 * @date 2023/2/21
 * @implNote IArrayRef
 */
public interface IArrayRef extends IRef {
    default void setChanged(int index) {
        markAsDirty();
    }

    IntSet getChanged();

}
