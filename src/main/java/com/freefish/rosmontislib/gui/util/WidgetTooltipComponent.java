/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.gui.util;

import com.freefish.rosmontislib.gui.widget.Widget;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author KilaBash
 * @date 2023/6/29
 * @implNote WidgetTooltipComponent
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record WidgetTooltipComponent(Widget widget) implements TooltipComponent {
}
