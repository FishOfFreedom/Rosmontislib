/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.editor.configurator;

import com.freefish.rosmontislib.gui.editor.ColorPattern;
import com.freefish.rosmontislib.gui.editor.Icons;
import com.freefish.rosmontislib.gui.editor.ui.Editor;
import com.freefish.rosmontislib.gui.modular.ModularUIContainer;
import com.freefish.rosmontislib.gui.texture.ColorBorderTexture;
import com.freefish.rosmontislib.gui.texture.ColorRectTexture;
import com.freefish.rosmontislib.gui.texture.GuiTextureGroup;
import com.freefish.rosmontislib.gui.texture.IGuiTexture;
import com.freefish.rosmontislib.gui.util.TreeBuilder;
import com.freefish.rosmontislib.gui.widget.ButtonWidget;
import com.freefish.rosmontislib.gui.widget.DialogWidget;
import com.freefish.rosmontislib.gui.widget.HsbColorWidget;
import com.freefish.rosmontislib.gui.widget.ImageWidget;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2022/12/1
 * @implNote NumberConfigurator
 */
public class ColorConfigurator extends ValueConfigurator<Number> {
    protected ImageWidget image;
    @Setter
    protected boolean colorBackground;

    public ColorConfigurator(String name, Supplier<Number> supplier, Consumer<Number> onUpdate, @Nonnull Number defaultValue, boolean forceUpdate) {
        super(name, supplier, onUpdate, defaultValue, forceUpdate);
        if (value == null) {
            value = defaultValue;
        }
    }

    @Override
    protected void onValueUpdate(Number newValue) {
        if (newValue == null) newValue = defaultValue;
        if (newValue.equals(value)) return;
        super.onValueUpdate(newValue);
        image.setImage(getCommonColor());
    }

    private IGuiTexture getCommonColor() {
        return new GuiTextureGroup(new ColorRectTexture(value.intValue()).setRadius(5).setRadius(5),
                new ColorBorderTexture(ColorPattern.WHITE.color, -1).setRadius(5));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 1 && Editor.INSTANCE != null && image.isMouseOverElement(mouseX, mouseY)) {
            var menu = TreeBuilder.Menu.start()
                    .leaf(Icons.COPY, "ldlib.gui.editor.menu.copy", () -> Editor.INSTANCE.setCopy("number", value));
            if ("number".equals(Editor.INSTANCE.getCopyType())) {
                menu.leaf(Icons.PASTE, "ldlib.gui.editor.menu.paste", () -> {
                    Editor.INSTANCE.ifCopiedPresent("number", c -> {
                        if (c instanceof Number number) {
                            onValueUpdate(number.intValue());
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
        addWidget(image = new ImageWidget(leftWidth, 2, width - leftWidth - 3 - rightWidth, 10, getCommonColor()));
        image.setDraggingConsumer(
                o -> o instanceof Number,
                o -> image.setImage(ColorPattern.GREEN.rectTexture().setRadius(5)),
                o -> image.setImage(getCommonColor()),
                o -> {
                    if (o instanceof Number number) {
                        onValueUpdate(number.intValue());
                        updateValue();
                    }
                });
        addWidget(new ButtonWidget(leftWidth, 2, width - leftWidth - 3 - rightWidth, 10, null, cd -> {
            var position = image.getPosition();
            var rightPlace = getGui().getScreenWidth() - 110;
            var dialog = new DialogWidget(Math.min(position.x, rightPlace), position.y - 110, 110, 110);
            dialog.setClickClose(true);
            dialog.addWidget(new HsbColorWidget(5, 5, 100, 100)
                            .setOnChanged(newColor -> {
                                value = newColor;
                                updateValue();
                                image.setImage(getCommonColor());
                            })
                            .setColorSupplier(() -> value.intValue())
                            .setColor(value.intValue()))
                    .setBackground(new GuiTextureGroup(ColorPattern.BLACK.rectTexture(), ColorPattern.T_WHITE.borderTexture(-1)));
            if (Editor.INSTANCE != null) {
                Editor.INSTANCE.openDialog(dialog);
            } else {
                if (Minecraft.getInstance().player.containerMenu instanceof ModularUIContainer container) {
                    container.getModularUI().mainGroup.addWidget(dialog);
                }
            }
        }));
    }

}
