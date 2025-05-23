/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.utils;

import net.minecraft.world.entity.player.Inventory;

public class PlayerInventoryTransfer extends ContainerTransfer {
    public PlayerInventoryTransfer(Inventory inv) {
        super(inv);
    }

    @Override
    public int getSlots() {
        return ((Inventory) getInv()).items.size();
    }
}
