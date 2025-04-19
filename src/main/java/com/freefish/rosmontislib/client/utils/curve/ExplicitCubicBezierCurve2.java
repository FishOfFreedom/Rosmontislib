package com.freefish.rosmontislib.client.utils.curve;

import com.freefish.rosmontislib.client.utils.Interpolations;
import net.minecraft.world.phys.Vec2;

public class ExplicitCubicBezierCurve2 extends Curve<Vec2>{
    public Vec2 p0, c0, c1, p1;

    public ExplicitCubicBezierCurve2(Vec2 start, Vec2 control1, Vec2 control2, Vec2 end) {
        this.p0 = start;
        this.c0 = control1;
        this.c1 = control2;
        this.p1 = end;
    }

    @Override
    public Vec2 getPoint(float t) {
        if (c0.x == p0.x) {
            return new Vec2(p0.x + t * (p1.x - p0.x), c0.y > p0.y ? p0.y : p1.y);
        }
        if (c1.x == p1.x) {
            return new Vec2(p0.x + t * (p1.x - p0.x), c1.y > p1.y ? p1.y : p0.y);
        }
        return new Vec2(
                p0.x + t * (p1.x - p0.x),
                (float) Interpolations.CubicBezier(t, p0.y, c0.y, c1.y, p1.y)
        );
    }
}