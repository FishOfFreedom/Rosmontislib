/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.texture;

import com.freefish.rosmontislib.gui.editor.configurator.ConfiguratorGroup;
import com.freefish.rosmontislib.gui.editor.data.resource.Resource;
import com.mojang.datafixers.util.Either;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.File;

/**
 * @author KilaBash
 * @date 2022/12/15
 * @implNote UIResourceTexture
 */
public class UIResourceTexture implements IGuiTexture {
    @Getter
    private static Resource<IGuiTexture> projectResource;
    @Getter
    private static boolean isProject;

    public static void setCurrentResource(Resource<IGuiTexture> resource, boolean isProject) {
        projectResource = resource;
        UIResourceTexture.isProject = isProject;
    }

    public static void clearCurrentResource() {
        projectResource = null;
        UIResourceTexture.isProject = false;
    }

    @Setter
    private Resource<IGuiTexture> resource;

    public final Either<String, File> key;

    public UIResourceTexture(Either<String, File> key) {
        this.key = key;
    }

    public UIResourceTexture(Resource<IGuiTexture> resource, Either<String, File> key) {
        this.resource = resource;
        this.key = key;
    }

    public IGuiTexture getTexture() {
        return resource == null ? IGuiTexture.MISSING_TEXTURE : resource.getResourceOrDefault(key, IGuiTexture.MISSING_TEXTURE);
    }

    @Override
    public IGuiTexture setColor(int color) {
        return getTexture().setColor(color);
    }

    @Override
    public IGuiTexture rotate(float degree) {
        return getTexture().rotate(degree);
    }

    @Override
    public IGuiTexture scale(float scale) {
        return getTexture().scale(scale);
    }

    @Override
    public IGuiTexture transform(int xOffset, int yOffset) {
        return getTexture().transform(xOffset, yOffset);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void draw(GuiGraphics graphics, int mouseX, int mouseY, float x, float y, int width, int height) {
        getTexture().draw(graphics, mouseX, mouseY, x, y, width, height);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateTick() {
        getTexture().updateTick();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawSubArea(GuiGraphics graphics, float x, float y, float width, float height, float drawnU, float drawnV, float drawnWidth, float drawnHeight) {
        getTexture().drawSubArea(graphics, x, y, width, height, drawnU, drawnV, drawnWidth, drawnHeight);
    }

    @Override
    public void createPreview(ConfiguratorGroup father) {
        getTexture().createPreview(father);
    }

    @Override
    public void buildConfigurator(ConfiguratorGroup father) {
        getTexture().buildConfigurator(father);
    }

    @Override
    public void setUIResource(Resource<IGuiTexture> texturesResource) {
        setResource(texturesResource);
    }
}
