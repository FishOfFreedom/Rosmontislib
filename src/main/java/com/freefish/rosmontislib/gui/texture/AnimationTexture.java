/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.texture;

import com.freefish.rosmontislib.gui.editor.ColorPattern;
import com.freefish.rosmontislib.gui.editor.annotation.Configurable;
import com.freefish.rosmontislib.gui.editor.annotation.LDLRegister;
import com.freefish.rosmontislib.gui.editor.annotation.NumberColor;
import com.freefish.rosmontislib.gui.editor.annotation.NumberRange;
import com.freefish.rosmontislib.gui.editor.configurator.ConfiguratorGroup;
import com.freefish.rosmontislib.gui.editor.configurator.WrapperConfigurator;
import com.freefish.rosmontislib.gui.editor.ui.Editor;
import com.freefish.rosmontislib.gui.widget.ButtonWidget;
import com.freefish.rosmontislib.gui.widget.DialogWidget;
import com.freefish.rosmontislib.gui.widget.ImageWidget;
import com.freefish.rosmontislib.gui.widget.WidgetGroup;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.File;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_TEX_COLOR;

/**
 * @author KilaBash
 * @date 2022/9/14
 * @implNote AnimationTexture
 */
@LDLRegister(name = "animation_texture", group = "texture")
public class AnimationTexture extends TransformTexture {

    @Configurable(name = "ldlib.gui.editor.name.resource")
    public ResourceLocation imageLocation;

    @Configurable(tips = "ldlib.gui.editor.tips.cell_size")
    @NumberRange(range = {1, Integer.MAX_VALUE})
    @Getter
    protected int cellSize;

    @Configurable(tips = "ldlib.gui.editor.tips.cell_from")
    @NumberRange(range = {0, Integer.MAX_VALUE})
    @Getter
    protected int from;

    @Configurable(tips = "ldlib.gui.editor.tips.cell_to")
    @NumberRange(range = {0, Integer.MAX_VALUE})
    @Getter
    protected int to;

    @Configurable(tips = "ldlib.gui.editor.tips.cell_animation")
    @NumberRange(range = {0, Integer.MAX_VALUE})
    @Getter
    protected int animation;

    @Configurable
    @NumberColor
    @Getter
    protected int color = -1;

    protected int currentFrame;

    protected int currentTime;
    private long lastTick;

    public AnimationTexture() {
        this("rosmontislib:textures/gui/particles.png");
        setCellSize(8).setAnimation(32,  44).setAnimation(1);
    }

    public AnimationTexture(String imageLocation) {
        this.imageLocation = new ResourceLocation(imageLocation);
    }

    public AnimationTexture(ResourceLocation imageLocation) {
        this.imageLocation = imageLocation;
    }

    public AnimationTexture copy() {
        return new AnimationTexture(imageLocation).setCellSize(cellSize).setAnimation(from, to).setAnimation(animation).setColor(color);
    }

    public AnimationTexture setTexture(String imageLocation) {
        this.imageLocation = new ResourceLocation(imageLocation);
        return this;
    }

    public AnimationTexture setCellSize(int cellSize) {
        this.cellSize = cellSize;
        return this;
    }

    public AnimationTexture setAnimation(int from, int to) {
        this.currentFrame = from;
        this.from = from;
        this.to = to;
        return this;
    }

    public AnimationTexture setAnimation(int animation) {
        this.animation = animation;
        return this;
    }

    @Override
    public AnimationTexture setColor(int color) {
        this.color = color;
        return this;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateTick() {
        if (Minecraft.getInstance().level != null) {
            long tick = Minecraft.getInstance().level.getGameTime();
            if (tick == lastTick) return;
            lastTick = tick;
        }
        if (currentTime >= animation) {
            currentTime = 0;
            currentFrame += 1;
        } else {
            currentTime++;
        }
        if (currentFrame > to) {
            currentFrame = from;
        } else if (currentFrame < from) {
            currentFrame = from;
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void drawInternal(GuiGraphics graphics, int mouseX, int mouseY, float x, float y, int width, int height) {
        updateTick();
        float cell = 1f / this.cellSize;
        int X = currentFrame % cellSize;
        int Y = currentFrame / cellSize;

        float imageU = X * cell;
        float imageV = Y * cell;

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, imageLocation);
        var matrix4f = graphics.pose().last().pose();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, POSITION_TEX_COLOR);
        bufferbuilder.vertex(matrix4f, x, y + height, 0).uv(imageU, imageV + cell).color(color).endVertex();
        bufferbuilder.vertex(matrix4f, x + width, y + height, 0).uv(imageU + cell, imageV + cell).color(color).endVertex();
        bufferbuilder.vertex(matrix4f, x + width, y, 0).uv(imageU + cell, imageV).color(color).endVertex();
        bufferbuilder.vertex(matrix4f, x, y, 0).uv(imageU, imageV).color(color).endVertex();
        tessellator.end();
    }

    @Override
    public void createPreview(ConfiguratorGroup father) {
        super.createPreview(father);
        WrapperConfigurator base = new WrapperConfigurator("ldlib.gui.editor.group.base_image", wrapper -> {
            WidgetGroup widgetGroup = new WidgetGroup(0, 0, 100, 100);
            ImageWidget imageWidget;
            widgetGroup.addWidget(imageWidget = new ImageWidget(0, 0, 100, 100, new GuiTextureGroup(new ResourceTexture(imageLocation.toString()), this::drawGuides)).setBorder(2, ColorPattern.T_WHITE.color));
            widgetGroup.addWidget(new ButtonWidget(0, 0, 100, 100, IGuiTexture.EMPTY, cd -> {
                if (Editor.INSTANCE == null) return;
                File path = new File(Editor.INSTANCE.getWorkSpace(), "textures");
                DialogWidget.showFileDialog(Editor.INSTANCE, "ldlib.gui.editor.tips.select_image", path, true,
                        DialogWidget.suffixFilter(".png"), r -> {
                            if (r != null && r.isFile()) {
                                imageLocation = getTextureFromFile(path, r);
                                cellSize = 1;
                                from = 0;
                                to = 0;
                                animation = 0;
                                imageWidget.setImage(new GuiTextureGroup(new ResourceTexture(imageLocation.toString()), this::drawGuides));
                                wrapper.notifyChanges();
                            }
                        });
            }));
            return widgetGroup;
        });
        base.setTips("ldlib.gui.editor.tips.click_select_image");
        father.addConfigurators(base);
    }

    private ResourceLocation getTextureFromFile(File path, File r){
        var id = path.getPath().replace('\\', '/').split("assets/")[1].split("/")[0];
        return new ResourceLocation(id, r.getPath().replace(path.getPath(), "textures").replace('\\', '/'));
    }

    @OnlyIn(Dist.CLIENT)
    protected void drawGuides(GuiGraphics graphics, int mouseX, int mouseY, float x, float y, int width, int height) {
        float cell = 1f / this.cellSize;
        int X = from % cellSize;
        int Y = from / cellSize;

        float imageU = X * cell;
        float imageV = Y * cell;

        new ColorBorderTexture(-1, 0xff00ff00).draw(graphics, 0, 0,
                x + width * imageU, y + height * imageV,
                (int) (width * (cell)), (int) (height * (cell)));

        X = to % cellSize;
        Y = to / cellSize;

        imageU = X * cell;
        imageV = Y * cell;

        new ColorBorderTexture(-1, 0xffff0000).draw(graphics, 0, 0,
                x + width * imageU, y + height * imageV,
                (int) (width * (cell)), (int) (height * (cell)));
    }
}
