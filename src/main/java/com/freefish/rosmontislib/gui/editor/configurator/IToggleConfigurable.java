/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.editor.configurator;


import com.freefish.rosmontislib.gui.editor.ColorPattern;
import com.freefish.rosmontislib.gui.texture.ColorBorderTexture;
import com.freefish.rosmontislib.gui.texture.ColorRectTexture;
import com.freefish.rosmontislib.gui.texture.GuiTextureGroup;
import com.freefish.rosmontislib.gui.widget.SwitchWidget;

/**
 * @author KilaBash
 * @date 2023/5/30
 * @implNote IToggleConfigurable
 */
public interface IToggleConfigurable extends IConfigurable {

    boolean isEnable();

    void setEnable(boolean enable);

    @Override
    default void buildConfigurator(ConfiguratorGroup father) {
        IConfigurable.super.buildConfigurator(father);
        if (!isEnable()) {
            father.setCanCollapse(false);
            var name = father.getNameWidget();
            if (name != null) {
                name.setTextColor(ColorPattern.GRAY.color);
            }
        } else {
            father.setCanCollapse(true);
            var name = father.getNameWidget();
            if (name != null) {
                name.setTextColor(ColorPattern.WHITE.color);
            }
        }
        father.addWidget(new SwitchWidget(father.getLeftWidth() + 12, 2, 10, 10, (cd, pressed) -> {
            setEnable(pressed);
            if (!isEnable()) {
                father.setCanCollapse(false);
                father.setCollapse(true);
                var name = father.getNameWidget();
                if (name != null) {
                    name.setTextColor(ColorPattern.GRAY.color);
                }
            } else {
                father.setCanCollapse(true);
                var name = father.getNameWidget();
                if (name != null) {
                    name.setTextColor(ColorPattern.WHITE.color);
                }
            }
        })
                .setPressed(isEnable())
                .setTexture(new ColorBorderTexture(-1, -1).setRadius(5),
                        new GuiTextureGroup(new ColorBorderTexture(-1, -1).setRadius(5),
                                new ColorRectTexture(-1).setRadius(5).scale(0.5f)))
                .setHoverTooltips("ldlib.gui.editor.toggle_configurable.tooltip"));
    }

}
