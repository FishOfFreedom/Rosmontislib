/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.editor.accessors;

import com.freefish.rosmontislib.gui.editor.annotation.ConfigAccessor;
import com.freefish.rosmontislib.gui.editor.annotation.DefaultValue;
import com.freefish.rosmontislib.gui.editor.configurator.Configurator;
import com.freefish.rosmontislib.gui.editor.configurator.StringConfigurator;
import com.google.common.collect.Lists;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.SnbtPrinterTagVisitor;
import net.minecraft.nbt.TagParser;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ConfigAccessor
public class CompoundTagAccessor extends TypesAccessor<CompoundTag> {

    public CompoundTagAccessor() {
        super(CompoundTag.class);
    }

    @Override
    public CompoundTag defaultValue(Field field, Class<?> type) {
        if (field.isAnnotationPresent(DefaultValue.class)) {
            try {
                return NbtUtils.snbtToStructure(field.getAnnotation(DefaultValue.class).stringValue()[0]);
            } catch (Exception e) {
                return new CompoundTag();
            }
        }
        return new CompoundTag();
    }

    @Override
    public Configurator create(String name, Supplier<CompoundTag> supplier, Consumer<CompoundTag> consumer, boolean forceUpdate, Field field) {
        var configurator = new StringConfigurator(name,
                () -> new SnbtPrinterTagVisitor("  ", 0, Lists.newArrayList()).visit(supplier.get()).replaceAll("\t", "").replaceAll("\\n", ""),
                text -> {
                    try {
                        var newTag = TagParser.parseTag(text);
                        var outTag = supplier.get();
                        if (newTag.equals(outTag)) return;
                        consumer.accept(newTag);
                    } catch (CommandSyntaxException ignored) {}
                }, new SnbtPrinterTagVisitor("  ", 0, Lists.newArrayList()).visit(defaultValue(field, String.class)).replaceAll("\t", "").replaceAll("\\n", ""), forceUpdate);
        configurator.setCompoundTag(true);
        return configurator;
    }
}
