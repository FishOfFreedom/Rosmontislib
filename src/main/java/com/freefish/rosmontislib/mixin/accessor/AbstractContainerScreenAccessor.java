/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.mixin.accessor;


import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenAccessor {
    @Accessor ItemStack getDraggingItem();
    @Accessor int getQuickCraftingRemainder();
    @Accessor("isSplittingStack") boolean isSplittingStack();
    @Accessor ItemStack getSnapbackItem();
    @Accessor void setSnapbackItem(ItemStack item);
    @Accessor int getSnapbackStartX();
    @Accessor int getSnapbackStartY();
    @Accessor Slot getSnapbackEnd();
    @Accessor long getSnapbackTime();

}