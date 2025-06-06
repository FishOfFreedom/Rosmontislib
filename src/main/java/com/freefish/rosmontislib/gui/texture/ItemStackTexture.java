/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.texture;

import com.freefish.rosmontislib.gui.editor.annotation.Configurable;
import com.freefish.rosmontislib.gui.editor.annotation.LDLRegister;
import com.freefish.rosmontislib.gui.util.DrawerHelper;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LDLRegister(name = "item_stack_texture", group = "texture")
public class ItemStackTexture extends TransformTexture {
    @Configurable(name = "ldlib.gui.editor.name.items")
    public ItemStack[] items;
    private int index = 0;
    private int ticks = 0;
    @Configurable(name = "ldlib.gui.editor.name.color")
    @Getter
    private int color = -1;
    private long lastTick;

    public ItemStackTexture() {
        this(Items.APPLE.asItem());
    }


    public ItemStackTexture(ItemStack... itemStacks) {
        this.items = itemStacks;
    }

    public ItemStackTexture(Item... items) {
        this.items = new ItemStack[items.length];
        for(int i = 0; i < items.length; i++) {
            this.items[i] = new ItemStack(items[i]);
        }
    }

    public ItemStackTexture setItems(ItemStack... itemStack) {
        this.items = itemStack;
        return this;
    }

    @Override
    public ItemStackTexture setColor(int color) {
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
        if(items.length > 1 && ++ticks % 20 == 0)
            if(++index == items.length)
                index = 0;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void drawInternal(GuiGraphics graphics, int mouseX, int mouseY, float x, float y, int width, int height) {
        if (items.length == 0) return;
        updateTick();
        graphics.pose().pushPose();
        graphics.pose().scale(width / 16f, height / 16f, 1);
        graphics.pose().translate(x * 16 / width, y * 16 / height, -200);
        DrawerHelper.drawItemStack(graphics, items[index], 0, 0, color, null);
        graphics.pose().popPose();
    }
}
