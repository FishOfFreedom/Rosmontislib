/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.editor.data.resource;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.client.renderer.IRenderer;
import com.freefish.rosmontislib.gui.editor.annotation.LDLRegister;
import com.freefish.rosmontislib.gui.editor.ui.ResourcePanel;
import com.freefish.rosmontislib.gui.editor.ui.resource.IRendererResourceContainer;
import com.freefish.rosmontislib.gui.editor.ui.resource.ResourceContainer;
import com.freefish.rosmontislib.gui.widget.Widget;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;

import java.io.File;

@LDLRegister(name = IRendererResource.RESOURCE_NAME, group = "resource")
public class IRendererResource extends Resource<IRenderer> {
    //todo uselessWidget
    public final static String RESOURCE_NAME = "ldlib.gui.editor.group.renderer";

    public IRendererResource() {
        super(new File(RosmontisLib.getRLLibDir(), "assets/resources/renderers"));
        addBuiltinResource("empty", IRenderer.EMPTY);
    }

    @Override
    public void buildDefault() {
        //addBuiltinResource("furnace", new IModelRenderer(new ResourceLocation("block/furnace")));
    }

    @Override
    public String name() {
        return RESOURCE_NAME;
    }

    @Override
    public ResourceContainer<IRenderer, ? extends Widget> createContainer(ResourcePanel resourcePanel) {
        return new IRendererResourceContainer(this, resourcePanel);
    }

    @Nullable
    @Override
    public Tag serialize(IRenderer renderer) {
        //if (renderer instanceof ISerializableRenderer serializableRenderer) {
        //    return ISerializableRenderer.serializeWrapper(serializableRenderer);
        //}
        return null;
    }

    @Override
    public IRenderer deserialize(Tag tag) {
        if (tag instanceof CompoundTag compoundTag) {
            //var renderer = ISerializableRenderer.deserializeWrapper(compoundTag);
            //if (renderer != null) {
            //    return renderer;
            //}
        }
        return IRenderer.EMPTY;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getBuiltinResources().clear();
        addBuiltinResource("empty", IRenderer.EMPTY);
        for (String key : nbt.getAllKeys()) {
            addBuiltinResource(key, deserialize(nbt.get(key)));
        }
    }
}
