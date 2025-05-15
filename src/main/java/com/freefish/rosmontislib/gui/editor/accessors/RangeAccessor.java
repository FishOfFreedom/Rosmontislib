/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.editor.accessors;


import com.freefish.rosmontislib.client.utils.Range;
import com.freefish.rosmontislib.gui.editor.annotation.ConfigAccessor;
import com.freefish.rosmontislib.gui.editor.annotation.NumberRange;
import com.freefish.rosmontislib.gui.editor.configurator.Configurator;
import com.freefish.rosmontislib.gui.editor.configurator.RangeConfigurator;
import com.freefish.rosmontislib.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2022/12/1
 * @implNote RangeAccessor
 */
@ConfigAccessor
public class RangeAccessor extends TypesAccessor<Range> {

    public RangeAccessor() {
        super(Range.class);
    }

    @Override
    public Range defaultValue(Field field, Class<?> type) {
        if (field.isAnnotationPresent(NumberRange.class)) {
            var range = field.getAnnotation(NumberRange.class);
            if (range.range().length > 1) {
                return new Range(range.range()[0], range.range()[1]);
            }
        }
        return new Range(0f, 1f);
    }

    @Override
    public Configurator create(String name, Supplier<Range> supplier, Consumer<Range> consumer, boolean forceUpdate, Field field) {
        var configurator = new RangeConfigurator(name, supplier, consumer, defaultValue(field, ReflectionUtils.getRawType(field.getGenericType())), forceUpdate);
        if (field.isAnnotationPresent(NumberRange.class)) {
            NumberRange range = field.getAnnotation(NumberRange.class);
            configurator = configurator.setRange(range.range()[0], range.range()[1]).setWheel(range.wheel());
        }
        return configurator;
    }
}
