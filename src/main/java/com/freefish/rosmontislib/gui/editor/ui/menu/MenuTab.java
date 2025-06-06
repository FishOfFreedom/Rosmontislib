/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.editor.ui.menu;

import com.freefish.rosmontislib.gui.editor.ColorPattern;
import com.freefish.rosmontislib.gui.editor.ILDLRegister;
import com.freefish.rosmontislib.gui.editor.ui.Editor;
import com.freefish.rosmontislib.gui.texture.TextTexture;
import com.freefish.rosmontislib.gui.util.TreeBuilder;
import com.freefish.rosmontislib.gui.widget.ButtonWidget;
import com.freefish.rosmontislib.gui.widget.Widget;
import com.freefish.rosmontislib.utils.LocalizationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author KilaBash
 * @date 2022/12/17
 * @implNote MenuTab
 */
public abstract class MenuTab implements ILDLRegister {
    private final static Map<String, List<BiConsumer<MenuTab, TreeBuilder.Menu>>> HOOKS = new LinkedHashMap<>();

    protected Editor editor;

    protected MenuTab() {
        this.editor = Editor.INSTANCE;
        if (this.editor == null) {
            throw new RuntimeException("editor is null while creating a menu tab %s".formatted(name()));
        }
    }

    public static void registerMenuHook(String menuName, BiConsumer<MenuTab, TreeBuilder.Menu> consumer) {
        HOOKS.computeIfAbsent(menuName, n -> new ArrayList<>()).add(consumer);
    }

    public TreeBuilder.Menu appendMenu(TreeBuilder.Menu menu) {
        for (var hook : HOOKS.getOrDefault(name(), Collections.emptyList())) {
            hook.accept(this, menu);
        }
        return menu;
    }

    @OnlyIn(Dist.CLIENT)
    public Widget createTabWidget() {
        int width = Minecraft.getInstance().font.width(LocalizationUtils.format(getTranslateKey()));
        var button = new ButtonWidget(0, 0, width + 6, 16, new TextTexture(getTranslateKey()), null)
                .setHoverTexture(ColorPattern.T_WHITE.rectTexture(), new TextTexture(getTranslateKey()));
        button.setOnPressCallback(cd -> {
            var pos = button.getPosition();
            var view = createMenu();
            if (view != null) {
                var currentProject = editor.getCurrentProject();
                if (currentProject != null) {
                    currentProject.attachMenu(editor, this.name(), view);
                }
                editor.openMenu(pos.x, pos.y + 14, appendMenu(view));
            }
        });
        return button.setClientSideWidget();
    }

    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    public void deserializeNBT(CompoundTag nbt) {

    }

    @Nullable
    abstract protected TreeBuilder.Menu createMenu();
}
