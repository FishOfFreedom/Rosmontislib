/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.editor.configurator;

import com.freefish.rosmontislib.client.utils.Size;
import com.freefish.rosmontislib.gui.editor.ColorPattern;
import com.freefish.rosmontislib.gui.editor.Icons;
import com.freefish.rosmontislib.gui.editor.ui.Editor;
import com.freefish.rosmontislib.gui.texture.ColorRectTexture;
import com.freefish.rosmontislib.gui.texture.IGuiTexture;
import com.freefish.rosmontislib.gui.texture.TextTexture;
import com.freefish.rosmontislib.gui.util.ClickData;
import com.freefish.rosmontislib.gui.util.TreeBuilder;
import com.freefish.rosmontislib.gui.widget.ButtonWidget;
import com.freefish.rosmontislib.gui.widget.ImageWidget;
import lombok.Setter;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2022/12/2
 * @implNote GuiTextureConfigurator
 */
public class GuiTextureConfigurator extends ValueConfigurator<IGuiTexture>{
    protected ImageWidget preview;
    @Setter
    protected Consumer<ClickData> onPressCallback;
    @Setter
    protected Predicate<IGuiTexture> available;

    public GuiTextureConfigurator(String name, Supplier<IGuiTexture> supplier, Consumer<IGuiTexture> onUpdate, boolean forceUpdate) {
        super(name, supplier, onUpdate, IGuiTexture.EMPTY, forceUpdate);
        if (value == null) {
            value = defaultValue;
        }
    }

    @Override
    protected void onValueUpdate(IGuiTexture newValue) {
        if (Objects.equals(newValue, value)) return;
        super.onValueUpdate(newValue);
        preview.setImage(newValue);
    }

    @Override
    public void computeHeight() {
        super.computeHeight();
        setSize(new Size(getSize().width, 15 + preview.getSize().height + 4));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 1 && Editor.INSTANCE != null && preview.isMouseOverElement(mouseX, mouseY)) {
            var menu = TreeBuilder.Menu.start()
                    .leaf(Icons.DELETE, "ldlib.gui.editor.menu.remove", () -> {
                        onValueUpdate(IGuiTexture.EMPTY);
                        updateValue();
                    })
                    .leaf(Icons.COPY, "ldlib.gui.editor.menu.copy", () -> Editor.INSTANCE.setCopy("texture", value));
            if ("texture".equals(Editor.INSTANCE.getCopyType())) {
                menu.leaf(Icons.PASTE, "ldlib.gui.editor.menu.paste", () -> {
                    Editor.INSTANCE.ifCopiedPresent("texture", c -> {
                        if (c instanceof IGuiTexture texture) {
                            onValueUpdate(texture);
                            updateValue();
                        }
                    });
                });
            }
            Editor.INSTANCE.openMenu(mouseX, mouseY, menu);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void init(int width) {
        super.init(width);
        int w = Math.min(width - 6, 50);
        int x = (width - w) / 2;
        addWidget(preview = new ImageWidget(x, 17, w, w, value).setBorder(2, ColorPattern.T_WHITE.color));
        preview.setDraggingConsumer(
                o -> available == null ? (o instanceof IGuiTexture || o instanceof Integer || o instanceof String) : (o instanceof IGuiTexture texture && available.test(texture)),
                o -> preview.setBorder(2, ColorPattern.GREEN.color),
                o -> preview.setBorder(2, ColorPattern.T_WHITE.color),
                o -> {
                    IGuiTexture newTexture = null;
                    if (available != null && o instanceof IGuiTexture texture && available.test(texture)) {
                        newTexture = texture;
                    }else if (o instanceof IGuiTexture texture) {
                        newTexture = texture;
                    } else if (o instanceof Integer color) {
                        newTexture = new ColorRectTexture(color);
                    } else if (o instanceof String string) {
                        newTexture = new TextTexture(string);
                    }
                    if (newTexture != null) {
                        onValueUpdate(newTexture);
                        updateValue();
                    }
                    preview.setBorder(2, ColorPattern.T_WHITE.color);
                });
        if (onPressCallback != null) {
            addWidget(new ButtonWidget(x, 17, w, w, IGuiTexture.EMPTY, onPressCallback));
        }
    }

}
