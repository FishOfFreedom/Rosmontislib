/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.utils.curve;

import com.freefish.rosmontislib.client.utils.Interpolations;
import net.minecraft.world.phys.Vec2;

public class CubicBezierCurve2 extends Curve<Vec2> {
    public Vec2 p0, c0, c1, p1;

    public CubicBezierCurve2(Vec2 start, Vec2 control1, Vec2 control2, Vec2 end) {
        this.p0 = start;
        this.c0 = control1;
        this.c1 = control2;
        this.p1 = end;
    }

    @Override
    public Vec2 getPoint(float t) {
        return new Vec2(
                (float) Interpolations.CubicBezier(t, p0.x, c0.x, c1.x, p1.x),
                (float) Interpolations.CubicBezier(t, p0.y, c0.y, c1.y, p1.y)
        );
    }
}
