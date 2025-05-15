/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.sync;

public interface IContentChangeAware {
    void setOnContentsChanged(Runnable onContentChanged);
    Runnable getOnContentsChanged();
}