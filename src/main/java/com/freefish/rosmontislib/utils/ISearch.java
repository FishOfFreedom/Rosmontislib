/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.utils;

import java.util.function.Consumer;

public interface ISearch<T> {
    void search(String word, Consumer<T> find);
}
