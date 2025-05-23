/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.ingredient;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public interface IGhostIngredientTarget {

    @OnlyIn(Dist.CLIENT)
    List<Target> getPhantomTargets(Object ingredient);

}
