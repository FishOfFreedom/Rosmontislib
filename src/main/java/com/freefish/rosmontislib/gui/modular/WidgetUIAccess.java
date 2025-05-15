/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.modular;

import com.freefish.rosmontislib.gui.widget.Widget;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public interface WidgetUIAccess {

    boolean attemptMergeStack(ItemStack itemStack, boolean fromContainer, boolean simulate);

    void writeClientAction(Widget widget, int id, Consumer<FriendlyByteBuf> payloadWriter);

    void writeUpdateInfo(Widget widget, int id, Consumer<FriendlyByteBuf> payloadWriter);

}
