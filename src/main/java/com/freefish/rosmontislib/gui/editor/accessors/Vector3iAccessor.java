/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.editor.accessors;

import com.freefish.rosmontislib.gui.editor.annotation.ConfigAccessor;
import com.freefish.rosmontislib.gui.editor.annotation.DefaultValue;
import com.freefish.rosmontislib.gui.editor.annotation.NumberRange;
import com.freefish.rosmontislib.gui.editor.configurator.Configurator;
import com.freefish.rosmontislib.gui.editor.configurator.Vector3iConfigurator;
import com.freefish.rosmontislib.utils.ReflectionUtils;
import org.joml.Vector3i;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2023/5/27
 * @implNote Vector3iAccessor
 */
@ConfigAccessor
public class Vector3iAccessor extends TypesAccessor<Vector3i> {

    public Vector3iAccessor() {
        super(Vector3i.class);
    }

    @Override
    public Vector3i defaultValue(Field field, Class<?> type) {
        if (field.isAnnotationPresent(DefaultValue.class)) {
            return new Vector3i((int) field.getAnnotation(DefaultValue.class).numberValue()[0], (int) field.getAnnotation(DefaultValue.class).numberValue()[1], (int) field.getAnnotation(DefaultValue.class).numberValue()[2]);
        }
        return new Vector3i(0, 0, 0);
    }

    @Override
    public Configurator create(String name, Supplier<Vector3i> supplier, Consumer<Vector3i> consumer, boolean forceUpdate, Field field) {
        var configurator = new Vector3iConfigurator(name, supplier, consumer, defaultValue(field, ReflectionUtils.getRawType(field.getGenericType())), forceUpdate);
        if (field.isAnnotationPresent(NumberRange.class)) {
            NumberRange range = field.getAnnotation(NumberRange.class);
            configurator = configurator.setRange((int)range.range()[0], (int)range.range()[1]).setWheel((int)Math.ceil(range.wheel()));
        }
        return configurator;
    }

}
