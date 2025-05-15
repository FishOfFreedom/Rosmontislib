/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.data.material;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author KilaBash
 * @date 2023/5/29
 * @implNote MaterialsResource
 */
@OnlyIn(Dist.CLIENT)
public interface IMaterial {

    void begin(boolean isInstancing);

    void end(boolean isInstancing);
}
