/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.editor.accessors;

import com.freefish.rosmontislib.client.renderer.IRenderer;
import com.freefish.rosmontislib.gui.editor.annotation.ConfigAccessor;
import com.freefish.rosmontislib.gui.editor.annotation.DefaultValue;
import com.freefish.rosmontislib.gui.editor.configurator.Configurator;
import com.freefish.rosmontislib.gui.editor.configurator.IRendererConfigurator;
import com.freefish.rosmontislib.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ConfigAccessor
public class IRendererAccessor extends TypesAccessor<IRenderer> {
    //todo uselessWidget

    public IRendererAccessor() {
        super(IRenderer.class);
    }

    @Override
    public IRenderer defaultValue(Field field, Class<?> type) {
        if (field.isAnnotationPresent(DefaultValue.class)) {
            //return new IModelRenderer(new ResourceLocation(field.getAnnotation(DefaultValue.class).stringValue()[0]));
        }
        return IRenderer.EMPTY;
    }

    @Override
    public Configurator create(String name, Supplier<IRenderer> supplier, Consumer<IRenderer> consumer, boolean forceUpdate, Field field) {
        return new IRendererConfigurator(name, supplier, consumer, defaultValue(field, ReflectionUtils.getRawType(field.getGenericType())), forceUpdate);
    }
}
