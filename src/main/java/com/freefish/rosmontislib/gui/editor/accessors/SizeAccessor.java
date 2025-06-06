/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.editor.accessors;


import com.freefish.rosmontislib.client.utils.Size;
import com.freefish.rosmontislib.gui.editor.annotation.ConfigAccessor;
import com.freefish.rosmontislib.gui.editor.annotation.Configurable;
import com.freefish.rosmontislib.gui.editor.annotation.DefaultValue;
import com.freefish.rosmontislib.gui.editor.configurator.Configurator;
import com.freefish.rosmontislib.gui.editor.configurator.ConfiguratorGroup;
import com.freefish.rosmontislib.gui.editor.configurator.NumberConfigurator;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2022/12/2
 * @implNote PositionAccessor
 */
@ConfigAccessor
public class SizeAccessor extends TypesAccessor<Size> {

    public SizeAccessor() {
        super(Size.class);
    }

    @Override
    public Size defaultValue(Field field, Class<?> type) {
        if (field.isAnnotationPresent(DefaultValue.class)) {
            return new Size((int) field.getAnnotation(DefaultValue.class).numberValue()[0], (int) field.getAnnotation(DefaultValue.class).numberValue()[1]);
        }
        return Size.ZERO;
    }

    @Override
    public Configurator create(String name, Supplier<Size> supplier, Consumer<Size> consumer, boolean forceUpdate, Field field) {
        ConfiguratorGroup group = new ConfiguratorGroup(name);
        if (field.isAnnotationPresent(Configurable.class)) {
            Configurable configurable = field.getAnnotation(Configurable.class);
            group.setCollapse(configurable.collapse());
            group.setCanCollapse(configurable.canCollapse());
            group.setTips(configurable.tips());
        }
        group.addConfigurators(new NumberConfigurator("width", () -> supplier.get().width, number -> consumer.accept(new Size(number.intValue(), supplier.get().height)), 0, forceUpdate).setRange(0, Integer.MAX_VALUE));
        group.addConfigurators(new NumberConfigurator("height", () -> supplier.get().height, number -> consumer.accept(new Size(supplier.get().width, number.intValue())), 0, forceUpdate).setRange(0, Integer.MAX_VALUE));
        return group;
    }

}
