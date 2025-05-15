/**
 * SPDX-License-Identifier: GPL-3.0
 * Copyright (c) 2023 KilaBash
 */

package com.freefish.rosmontislib.client.particle.advance.effect;

import net.minecraft.world.level.Level;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class FXEffect implements IFXEffect {
    @Override
    public Level getLevel() {
        return level;
    }

    public final Level level;

    @Override
    public void setOffset(Vector3f offset) {
        this.offset = offset;
    }

    @Override
    public void setRotation(Quaternionf rotation) {
        this.rotation = rotation;
    }

    @Override
    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    @Override
    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public void setForcedDeath(boolean forcedDeath) {
        this.forcedDeath = forcedDeath;
    }

    @Override
    public void setAllowMulti(boolean allowMulti) {
        this.allowMulti = allowMulti;
    }

    protected Vector3f offset = new Vector3f();

    protected Quaternionf rotation = new Quaternionf();

    protected Vector3f scale = new Vector3f(1, 1, 1);

    protected int delay;

    protected boolean forcedDeath;

    protected boolean allowMulti;

    protected FXEffect(Level level) {
        this.level = level;
    }
}