package com.freefish.rosmontislib.client.particle.advance.effect;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface IFXEffect extends IEffect {
    /**
     * get all emitters included in this effect.
     */
    //FX getFx();

    /**
     * set effect offset
     */
    default void setOffset(double x, double y, double z) {
        setOffset(new Vector3f((float) x, (float) y, (float) z));
    }

    /**
     * set effect rotation in degree
     */
    default void setRotation(double x, double y, double z) {
        setRotation(new Quaternionf().rotationXYZ((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z)));
    }

    /**
     * set effect scale
     */
    default void setScale(double x, double y, double z) {
        setScale(new Vector3f((float) x, (float) y, (float) z));
    }

    /**
     * set effect offset
     */
    void setOffset(Vector3f offset);

    /**
     * set effect rotation
     */
    void setRotation(Quaternionf quaternion);

    /**
     * set effect scale
     */
    void setScale(Vector3f scale);

    /**
     * set effect delay
     */
    void setDelay(int delay);

    /**
     * Whether to remove particles directly when the bound object invalid.
     * <br>
     * default - wait for particles death.
     */
    void setForcedDeath(boolean forcedDeath);

    /**
     * Allows multiple identical effects to be bound to a same object。
     */
    void setAllowMulti(boolean allowMulti);

    /**
     * start effect。
     */
    void start();

}