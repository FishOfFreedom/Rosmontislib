/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.utils;

import java.util.List;
import java.util.UUID;

/**
 * @author KilaBash
 * @date 2024/06/26
 * @implNote A scene object that can be placed in the scene editor.
 */
public interface ISceneObject {
    /**
     * Get the unique id of the object.
     */
    default UUID id() {
        return transform().getId();
    }

    /**
     * Get the transform of the object.
     */
    Transform transform();

    default void setTransform(Transform transform) {
        transform.set(transform);
    }

    /**
     * Called when the transform of the object is changed.
     */
    default void onTransformChanged() {
    }

    /**
     * Called when the children of the object is changed.
     */
    default void onChildChanged() {
    }

    /**
     * Called when the parent of the object is changed.
     */
    default void onParentChanged() {

    }

    /**
     * Update the interactable per tick.
     */
    default void updateTick() {
    }

    /**
     * Update the interactable per frame.
     */
    default void updateFrame(float partialTicks) {
    }

}