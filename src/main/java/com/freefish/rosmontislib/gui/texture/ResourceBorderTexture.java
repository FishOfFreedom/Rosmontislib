/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.texture;

import com.freefish.rosmontislib.client.utils.Size;
import com.freefish.rosmontislib.gui.editor.annotation.Configurable;
import com.freefish.rosmontislib.gui.editor.annotation.LDLRegister;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LDLRegister(name = "border_texture", group = "texture")
public class ResourceBorderTexture extends ResourceTexture {
    public static final ResourceBorderTexture BORDERED_BACKGROUND = new ResourceBorderTexture("rosmontislib:textures/gui/background.png", 16, 16, 4, 4);
    public static final ResourceBorderTexture BORDERED_BACKGROUND_INVERSE = new ResourceBorderTexture("rosmontislib:textures/gui/background_inverse.png", 16, 16, 4, 4);
    public static final ResourceBorderTexture BORDERED_BACKGROUND_BLUE = new ResourceBorderTexture("rosmontislib:textures/gui/bordered_background_blue.png", 195, 136, 4, 4);
    public static final ResourceBorderTexture BUTTON_COMMON = new ResourceBorderTexture("rosmontislib:textures/gui/button.png", 32, 32, 2, 2);
    public static final ResourceBorderTexture BAR = new ResourceBorderTexture("rosmontislib:textures/gui/button_common.png", 180, 20, 1, 1);
    public static final ResourceBorderTexture SELECTED = new ResourceBorderTexture("rosmontislib:textures/gui/selected.png", 16, 16, 2, 2);

    @Configurable(tips = {"ldlib.gui.editor.tips.corner_size.0", "ldlib.gui.editor.tips.corner_size.1"}, collapse = false)
    public Size borderSize;

    @Configurable(tips = "ldlib.gui.editor.tips.image_size", collapse = false)
    public Size imageSize;

    public ResourceBorderTexture() {
        this("rosmontislib:textures/gui/bordered_background_blue.png", 195, 136, 4, 4);
    }

    public ResourceBorderTexture(String imageLocation, int imageWidth, int imageHeight, int cornerWidth, int cornerHeight) {
        super(imageLocation);
        borderSize = new Size(cornerWidth, cornerHeight);
        imageSize = new Size(imageWidth, imageHeight);
    }

    public ResourceBorderTexture setBorderSize(int width, int height) {
        this.borderSize = new Size(width, height);
        return this;
    }

    public ResourceBorderTexture setImageSize(int width, int height) {
        this.imageSize = new Size(width, height);
        return this;
    }

    @Override
    public ResourceTexture copy() {
        return new ResourceBorderTexture(imageLocation.toString(), imageSize.width, imageSize.height, borderSize.width, borderSize.height);
    }

    @Override
    public ResourceBorderTexture setColor(int color) {
        super.setColor(color);
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void drawSubAreaInternal(GuiGraphics graphics, float x, float y, float width, float height, float drawnU, float drawnV, float drawnWidth, float drawnHeight) {
        //compute relative sizes
        float cornerWidth = borderSize.width * 1f / imageSize.width;
        float cornerHeight = borderSize.height * 1f / imageSize.height;
        //draw up corners
        super.drawSubAreaInternal(graphics, x, y, borderSize.width, borderSize.height, 0, 0, cornerWidth, cornerHeight);
        super.drawSubAreaInternal(graphics, x + width - borderSize.width, y, borderSize.width, borderSize.height, 1 - cornerWidth, 0, cornerWidth, cornerHeight);
        //draw down corners
        super.drawSubAreaInternal(graphics, x, y + height - borderSize.height, borderSize.width, borderSize.height, 0, 1 - cornerHeight, cornerWidth, cornerHeight);
        super.drawSubAreaInternal(graphics, x + width - borderSize.width, y + height - borderSize.height, borderSize.width, borderSize.height, 1 - cornerWidth, 1 - cornerHeight, cornerWidth, cornerHeight);
        //draw horizontal connections
        super.drawSubAreaInternal(graphics, x + borderSize.width, y, width - 2 * borderSize.width, borderSize.height,
                cornerWidth, 0, 1 - 2 * cornerWidth, cornerHeight);
        super.drawSubAreaInternal(graphics, x + borderSize.width, y + height - borderSize.height, width - 2 * borderSize.width, borderSize.height,
                cornerWidth, 1 - cornerHeight, 1 - 2 * cornerWidth, cornerHeight);
        //draw vertical connections
        super.drawSubAreaInternal(graphics, x, y + borderSize.height, borderSize.width, height - 2 * borderSize.height,
                0, cornerHeight, cornerWidth, 1 - 2 * cornerHeight);
        super.drawSubAreaInternal(graphics, x + width - borderSize.width, y + borderSize.height, borderSize.width, height - 2 * borderSize.height,
                1 - cornerWidth, cornerHeight, cornerWidth, 1 - 2 * cornerHeight);
        //draw central body
        super.drawSubAreaInternal(graphics, x + borderSize.width, y + borderSize.height,
                width - 2 * borderSize.width, height - 2 * borderSize.height,
                cornerWidth, cornerHeight, 1 - 2 * cornerWidth, 1 - 2 * cornerHeight);
    }

    @OnlyIn(Dist.CLIENT)
    protected void drawGuides(GuiGraphics graphics, int mouseX, int mouseY, float x, float y, int width, int height) {
        new ColorBorderTexture(-1, 0xffff0000).draw(graphics, 0, 0,
                x + width * offsetX, y + height * offsetY,
                (int) (width * imageWidth), (int) (height * imageHeight));

        float cornerWidth = borderSize.width * 1f / imageSize.width;
        float cornerHeight = borderSize.height * 1f / imageSize.height;

        new ColorBorderTexture(-1, 0xff00ff00).draw(graphics, 0, 0,
                x, y, (int) (width * cornerWidth), (int) (height * cornerHeight));
    }
}
