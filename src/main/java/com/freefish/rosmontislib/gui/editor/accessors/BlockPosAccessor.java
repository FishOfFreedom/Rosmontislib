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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import org.joml.Vector3i;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2023/5/27
 * @implNote BlockPosAccessor
 */
@ConfigAccessor
public class BlockPosAccessor extends TypesAccessor<Vec3i> {

    public BlockPosAccessor() {
        super(BlockPos.class, Vec3i.class);
    }

    @Override
    public boolean test(Class<?> type) {
        return Vec3i.class.isAssignableFrom(type);
    }

    @Override
    public BlockPos defaultValue(Field field, Class<?> type) {
        if (field.isAnnotationPresent(DefaultValue.class)) {
            return new BlockPos((int) field.getAnnotation(DefaultValue.class).numberValue()[0],
                    (int) field.getAnnotation(DefaultValue.class).numberValue()[1],
                    (int) field.getAnnotation(DefaultValue.class).numberValue()[2]);
        }
        return new BlockPos(0, 0, 0);
    }

    @Override
    public Configurator create(String name, Supplier<Vec3i> supplier, Consumer<Vec3i> consumer, boolean forceUpdate, Field field) {
        var defaultValue = defaultValue(field, ReflectionUtils.getRawType(field.getGenericType()));
        var configurator = new Vector3iConfigurator(name, () -> {
            var pos = supplier.get();
            pos = pos == null ? defaultValue : pos;
            return new Vector3i(pos.getX(), pos.getY(), pos.getZ());
        }, vec3 -> consumer.accept(new BlockPos(vec3.x(), vec3.y(), vec3.z())),
                new Vector3i(defaultValue.getX(), defaultValue.getY(), defaultValue.getZ()), forceUpdate);
        if (field.isAnnotationPresent(NumberRange.class)) {
            NumberRange range = field.getAnnotation(NumberRange.class);
            configurator = configurator.setRange((int)range.range()[0], (int)range.range()[1]).setWheel((int)Math.ceil(range.wheel()));
        }
        return configurator;
    }

}
