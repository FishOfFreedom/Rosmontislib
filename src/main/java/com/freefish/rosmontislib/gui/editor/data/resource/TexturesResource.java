/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.editor.data.resource;

import com.freefish.rosmontislib.RosmontisLib;
import com.freefish.rosmontislib.gui.editor.annotation.LDLRegister;
import com.freefish.rosmontislib.gui.editor.runtime.AnnotationDetector;
import com.freefish.rosmontislib.gui.editor.ui.ResourcePanel;
import com.freefish.rosmontislib.gui.editor.ui.resource.ResourceContainer;
import com.freefish.rosmontislib.gui.editor.ui.resource.TexturesResourceContainer;
import com.freefish.rosmontislib.gui.texture.IGuiTexture;
import com.freefish.rosmontislib.gui.texture.ResourceBorderTexture;
import com.freefish.rosmontislib.gui.widget.ImageWidget;
import com.freefish.rosmontislib.gui.widget.SlotWidget;
import com.freefish.rosmontislib.gui.widget.TankWidget;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.io.File;

import static com.freefish.rosmontislib.gui.widget.TabContainer.TABS_LEFT;

/**
 * @author KilaBash
 * @date 2022/12/3
 * @implNote TextureResource
 */
@LDLRegister(name = TexturesResource.RESOURCE_NAME, group = "resource")
public class TexturesResource extends Resource<IGuiTexture> {

    public final static String RESOURCE_NAME = "ldlib.gui.editor.group.textures";

    public TexturesResource() {
        super(new File(RosmontisLib.getRLLibDir(), "assets/resources/textures"));
        addBuiltinResource("empty", IGuiTexture.EMPTY);
    }

    @Override
    public void buildDefault() {
        addBuiltinResource("border background", ResourceBorderTexture.BORDERED_BACKGROUND);
        addBuiltinResource("button", ResourceBorderTexture.BUTTON_COMMON);
        addBuiltinResource("slot", SlotWidget.ITEM_SLOT_TEXTURE.copy());
        addBuiltinResource("fluid slot", TankWidget.FLUID_SLOT_TEXTURE.copy());
        addBuiltinResource("tab", TABS_LEFT.getSubTexture(0, 0, 0.5f, 1f / 3));
        addBuiltinResource("tab pressed", TABS_LEFT.getSubTexture(0.5f, 0, 0.5f, 1f / 3));
        for (var wrapper : AnnotationDetector.REGISTER_TEXTURES) {
            addBuiltinResource("ldlib.gui.editor.register.texture." + wrapper.annotation().name(), wrapper.creator().get());
        }
    }

    @Override
    public String name() {
        return RESOURCE_NAME;
    }

    @Override
    public ResourceContainer<IGuiTexture, ImageWidget> createContainer(ResourcePanel panel) {
        return new TexturesResourceContainer(this, panel);
    }

    @Override
    public Tag serialize(IGuiTexture value) {
        return IGuiTexture.serializeWrapper(value);
    }

    @Override
    public IGuiTexture deserialize(Tag nbt) {
        if (nbt instanceof CompoundTag tag) {
            return IGuiTexture.deserializeWrapper(tag);
        }
        return null;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getBuiltinResources().clear();
        addBuiltinResource("empty", IGuiTexture.EMPTY);
        for (String key : nbt.getAllKeys()) {
            addBuiltinResource(key, deserialize(nbt.get(key)));
        }
        for (IGuiTexture texture : getBuiltinResources().values()) {
            texture.setUIResource(this);
        }
    }

}
