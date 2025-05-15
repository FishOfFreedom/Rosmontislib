/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.sync;

/**
 * @author KilaBash
 * @date 2023/2/17
 * @implNote FieldUpdateListener
 */
@FunctionalInterface
public
interface IFieldUpdateListener<T> {
    void onFieldChanged(String changedField, T newValue, T oldValue);
}