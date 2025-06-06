/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.widget;

import com.freefish.rosmontislib.client.utils.Size;
import com.freefish.rosmontislib.gui.editor.annotation.ConfigSetter;
import com.freefish.rosmontislib.gui.editor.annotation.Configurable;
import com.freefish.rosmontislib.gui.editor.annotation.LDLRegister;
import com.freefish.rosmontislib.gui.editor.configurator.IConfigurableWidget;
import com.freefish.rosmontislib.gui.texture.TextTexture;
import lombok.Getter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Supplier;

@LDLRegister(name = "text_texture", group = "widget.basic")
public class TextTextureWidget extends Widget implements IConfigurableWidget {
    @Configurable(name = "ldlib.gui.editor.name.text")
    @Getter
    private Component lastComponent;
    @Configurable(name = "ldlib.gui.editor.name.text", subConfigurable = true)
    @Getter
    private final TextTexture textTexture = new TextTexture();
    private Supplier<Component> textSupplier;

    public TextTextureWidget() {
        this(0, 0, 50, 50, "Text");
    }

    public TextTextureWidget(int xPosition, int yPosition, int width, int height) {
        this(xPosition, yPosition, width, height, "");
    }

    public TextTextureWidget(int xPosition, int yPosition, int width, int height, String text) {
        super(xPosition, yPosition, width, height);
        textTexture.setDropShadow(true);
        textTexture.setWidth(width);
        textTexture.setSupplier(() -> lastComponent == null ? "" : lastComponent.getString());
        if (isRemote()) {
            lastComponent = Component.translatable(text);
        }
        setText(text);
    }

    @ConfigSetter(field = "lastComponent")
    public void setLastComponent(Component component) {
        this.lastComponent = component;
        setText(component);
    }

    @Override
    @ConfigSetter(field = "size")
    public void setSize(Size size) {
        super.setSize(size);
        textTexture.setWidth(size.width);
    }

    public TextTextureWidget textureStyle(Consumer<TextTexture> consumer) {
        consumer.accept(textTexture);
        return this;
    }

    public TextTextureWidget setText(String text) {
        textSupplier = () -> Component.translatable(text);
        return this;
    }

    public TextTextureWidget setText(Component text) {
        textSupplier = () -> text;
        return this;
    }

    public TextTextureWidget setText(Supplier<Component> textSupplier) {
        this.textSupplier = textSupplier;
        return this;
    }

    @Override
    public void writeInitialData(FriendlyByteBuf buffer) {
        super.writeInitialData(buffer);
        if (!isClientSideWidget) {
            buffer.writeBoolean(true);
            this.lastComponent = textSupplier.get();
            buffer.writeComponent(lastComponent);
        } else {
            buffer.writeBoolean(false);
        }
    }

    @Override
    public void readInitialData(FriendlyByteBuf buffer) {
        super.readInitialData(buffer);
        if (buffer.readBoolean()) {
            this.lastComponent = buffer.readComponent();
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (!isClientSideWidget) {
            var latest = textSupplier.get();
            if (!latest.equals(lastComponent)) {
                this.lastComponent = latest;
                writeUpdateInfo(-1, buffer -> buffer.writeComponent(this.lastComponent));
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        if (id == -1) {
            this.lastComponent = buffer.readComponent();
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateScreen() {
        super.updateScreen();
        if (isClientSideWidget) {
            var latest = textSupplier.get();
            if (!latest.equals(lastComponent)) {
                this.lastComponent = latest;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        var position = getPosition();
        var size = getSize();
        textTexture.draw(graphics, mouseX, mouseY, position.x, position.y, size.width, size.height);
        drawOverlay(graphics, mouseX, mouseY, partialTicks);
    }
}

