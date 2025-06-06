/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.data.material;

import java.util.function.Supplier;

public class MaterialType<T extends IMaterial> {
    public MaterialType(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    private final Supplier<T> supplier;

    public T create(){
        return supplier.get();
    }
}

