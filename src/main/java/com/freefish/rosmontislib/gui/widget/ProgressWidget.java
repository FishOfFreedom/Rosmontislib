/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.widget;

import com.freefish.rosmontislib.client.utils.Position;
import com.freefish.rosmontislib.client.utils.Size;
import com.freefish.rosmontislib.gui.editor.annotation.Configurable;
import com.freefish.rosmontislib.gui.editor.annotation.LDLRegister;
import com.freefish.rosmontislib.gui.editor.configurator.Configurator;
import com.freefish.rosmontislib.gui.editor.configurator.ConfiguratorGroup;
import com.freefish.rosmontislib.gui.editor.configurator.GuiTextureConfigurator;
import com.freefish.rosmontislib.gui.editor.configurator.IConfigurableWidget;
import com.freefish.rosmontislib.gui.texture.IGuiTexture;
import com.freefish.rosmontislib.gui.texture.ProgressTexture;
import com.freefish.rosmontislib.gui.texture.ResourceTexture;
import com.freefish.rosmontislib.gui.texture.UIResourceTexture;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.function.DoubleSupplier;
import java.util.function.Function;

@LDLRegister(name = "progress", group = "widget.basic")
@Accessors(chain = true)
public class ProgressWidget extends Widget implements IConfigurableWidget {
    public final static DoubleSupplier JEIProgress = () -> Math.abs(System.currentTimeMillis() % 2000) / 2000.;
    @Setter
    public DoubleSupplier progressSupplier;
    @Setter
    private Function<Double, String> dynamicHoverTips;
    @Configurable(name = "ldlib.gui.editor.name.progressTexture")
    @Setter
    private IGuiTexture progressTexture;
    @Configurable(name = "ldlib.gui.editor.name.overlayTexture")
    @Setter
    private IGuiTexture overlayTexture;
    @Getter
    private double lastProgressValue;

    public ProgressWidget() {
        this(JEIProgress, 0, 0, 40, 40, new ProgressTexture());
    }

    public ProgressWidget(DoubleSupplier progressSupplier, int x, int y, int width, int height, ResourceTexture fullImage) {
        super(new Position(x, y), new Size(width, height));
        this.progressSupplier = progressSupplier;
        this.progressTexture = new ProgressTexture(fullImage.getSubTexture(0.0, 0.0, 1.0, 0.5), fullImage.getSubTexture(0.0, 0.5, 1.0, 0.5));
        this.lastProgressValue = -1;
    }

    public ProgressWidget(DoubleSupplier progressSupplier, int x, int y, int width, int height, ProgressTexture progressBar) {
        super(new Position(x, y), new Size(width, height));
        this.progressSupplier = progressSupplier;
        this.progressTexture = progressBar;
        this.lastProgressValue = -1;
    }

    public ProgressWidget(DoubleSupplier progressSupplier, int x, int y, int width, int height) {
        super(new Position(x, y), new Size(width, height));
        this.progressSupplier = progressSupplier;
    }

    public ProgressWidget setProgressTexture(IGuiTexture emptyBarArea, IGuiTexture filledBarArea) {
        this.progressTexture = new ProgressTexture(emptyBarArea, filledBarArea);
        return this;
    }

    public ProgressWidget setFillDirection(ProgressTexture.FillDirection fillDirection) {
        if (this.progressTexture instanceof ProgressTexture progressTexture) {
            progressTexture.setFillDirection(fillDirection);
        }
        return this;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (this.progressTexture != null) {
            this.progressTexture.updateTick();
        }
        if (this.overlayTexture != null) {
            this.overlayTexture.updateTick();
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawInForeground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if ((!tooltipTexts.isEmpty() || dynamicHoverTips != null) && isMouseOverElement(mouseX, mouseY) && getHoverElement(mouseX, mouseY) == this && gui != null && gui.getModularUIGui() != null) {
            var tips = new ArrayList<>(tooltipTexts);
            if (dynamicHoverTips != null) {
                tips.add(Component.translatable(dynamicHoverTips.apply(lastProgressValue)));
            }
            gui.getModularUIGui().setHoverTooltip(tips, ItemStack.EMPTY, null, null);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawInBackground(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        if (progressSupplier == JEIProgress || isClientSideWidget) {
            lastProgressValue = progressSupplier.getAsDouble();
        }
        if (progressTexture instanceof ProgressTexture texture) {
            texture.setProgress(lastProgressValue);
        }
        Position pos = getPosition();
        Size size = getSize();
        if (progressTexture != null) {
            progressTexture.draw(graphics, mouseX, mouseY, pos.x, pos.y, size.width, size.height);
        }
        if (overlayTexture != null) {
            overlayTexture.draw(graphics, mouseX, mouseY, pos.x, pos.y, size.width, size.height);
        }
        drawOverlay(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        this.lastProgressValue = progressSupplier.getAsDouble();
    }

    @Override
    public void writeInitialData(FriendlyByteBuf buffer) {
        super.writeInitialData(buffer);
        buffer.writeDouble(lastProgressValue);
    }

    @Override
    public void readInitialData(FriendlyByteBuf buffer) {
        super.readInitialData(buffer);
        lastProgressValue = buffer.readDouble();
    }

    @Override
    public void detectAndSendChanges() {
        double actualValue = progressSupplier.getAsDouble();
        if (actualValue - lastProgressValue != 0) {
            this.lastProgressValue = actualValue;
            writeUpdateInfo(0, buffer -> buffer.writeDouble(actualValue));
        }
    }

    @Override
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        if (id == 0) {
            this.lastProgressValue = buffer.readDouble();
        }
    }

    @Override
    public void buildConfigurator(ConfiguratorGroup father) {
        IConfigurableWidget.super.buildConfigurator(father);
        for (Configurator configurator : father.getConfigurators()) {
            if (configurator instanceof GuiTextureConfigurator guiConfigurator && configurator.getName().equals("progressTexture")) {
                guiConfigurator.setOnUpdate(t -> {
                    if (t instanceof ProgressTexture || (t instanceof UIResourceTexture uiResourceTexture && uiResourceTexture.getTexture() instanceof ProgressTexture)) {
                        this.progressTexture = t;
                    }
                });
                guiConfigurator.setAvailable(t -> t instanceof ProgressTexture || (t instanceof UIResourceTexture uiResourceTexture && uiResourceTexture.getTexture() instanceof ProgressTexture));
                guiConfigurator.setTips("ldlib.gui.editor.tips.progress_texture");
            }
        }
    }

    @Override
    public boolean canDragIn(Object dragging) {
        if (dragging instanceof IGuiTexture) {
            return dragging instanceof ProgressTexture || (dragging instanceof UIResourceTexture uiResourceTexture && uiResourceTexture.getTexture() instanceof ProgressTexture);
        }
        return IConfigurableWidget.super.canDragIn(dragging);
    }

    @Override
    public boolean handleDragging(Object dragging) {
        if (dragging instanceof IGuiTexture) {
            if (dragging instanceof ProgressTexture || (dragging instanceof UIResourceTexture uiResourceTexture && uiResourceTexture.getTexture() instanceof ProgressTexture) ) {
                this.progressTexture = (IGuiTexture) dragging;
                return true;
            }
            return false;
        } else return IConfigurableWidget.super.handleDragging(dragging);
    }

}
