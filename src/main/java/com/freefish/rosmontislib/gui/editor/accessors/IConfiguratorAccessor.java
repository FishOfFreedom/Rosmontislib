/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.editor.accessors;


import com.freefish.rosmontislib.gui.editor.configurator.Configurator;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2022/12/1
 * @implNote IConfiguratorAccessor
 */
public interface IConfiguratorAccessor<T> {
    IConfiguratorAccessor<?> DEFAULT = type -> true;

    boolean test(Class<?> type);

    default T defaultValue(Field field, Class<?> type) {
        return null;
    }

    default Configurator create(String name, Supplier<T> supplier, Consumer<T> consumer, boolean forceUpdate, Field field) {
        return new Configurator(name);
    }
}
