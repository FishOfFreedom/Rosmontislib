/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.editor.configurator;


import com.freefish.rosmontislib.gui.editor.ColorPattern;
import com.freefish.rosmontislib.gui.texture.GuiTextureGroup;
import com.freefish.rosmontislib.gui.texture.TextTexture;
import com.freefish.rosmontislib.gui.widget.ButtonWidget;

import java.util.function.Supplier;

public class ButtonConfigurator extends Configurator{
    public Supplier<String> textSupplier;
    public Runnable runnable;
    private int lines = 1;

    public ButtonConfigurator(String text, Runnable runnable) {
        this.textSupplier = () -> text;
        this.runnable = runnable;
    }

    public ButtonConfigurator(Supplier<String> textSupplier, Runnable runnable) {
        this.textSupplier = textSupplier;
        this.runnable = runnable;
    }

    @Override
    public void init(int width) {
        super.init(width);
        var textTexture = new TextTexture(textSupplier).setWidth(width - rightWidth - 4);
        lines = textTexture.getLines();
        addWidget(new ButtonWidget(2, 2, width - rightWidth - 4, 10 * lines,
                new GuiTextureGroup(
                        ColorPattern.T_GRAY.rectTexture().setRadius(5),
                        textTexture),
                cd -> {
                    if (runnable != null) {
                        runnable.run();
                    }
                }));
    }

    @Override
    public void computeHeight() {
        setSizeHeight(lines * 10 + 5);
    }
}
