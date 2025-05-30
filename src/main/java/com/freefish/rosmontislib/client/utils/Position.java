/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.utils;

import com.google.common.base.MoreObjects;
import net.minecraft.world.phys.Vec2;
import org.joml.Vector2f;

import java.util.Objects;

public class Position {

    public static final Position ORIGIN = new Position(0, 0);

    public final int x;
    public final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Position of(int x, int y) {
        return new Position(x, y);
    }

    public Position add(Position other) {
        return new Position(x + other.x, y + other.y);
    }

    public Position add(int x, int y) {
        return new Position(this.x + x, this.y + y);
    }

    public Position subtract(Position other) {
        return new Position(x - other.x, y - other.y);
    }

    public Position add(Size size) {
        return new Position(x + size.width, y + size.height);
    }

    public Position addX(int x) {
        return new Position(this.x + x,y);
    }

    public Position addY(int y){
        return new Position(x,this.y + y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("x", x)
                .add("y", y)
                .toString();
    }

    public Vector2f vector2f() {
        return new Vector2f(x, y);
    }

    public Vec2 vec2() {
        return new Vec2(x, y);
    }
}
