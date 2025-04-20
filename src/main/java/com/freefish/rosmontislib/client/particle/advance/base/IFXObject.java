package com.freefish.rosmontislib.client.particle.advance.base;

import com.freefish.rosmontislib.client.particle.advance.effect.BlockEffect;
import com.freefish.rosmontislib.client.particle.advance.effect.IEffect;
import com.freefish.rosmontislib.client.utils.ISceneObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public interface IFXObject extends ISceneObject {
    /**
     * emitter name unique for one project
     */
    String getName();

    void setName(String name);

    Level getLevel();

    void setLevel(Level level);

    boolean isAlive();

    /**
     * should render particle
     */
    boolean isVisible();

    /**
     * set particle visible
     */
    void setVisible(boolean visible);

    void setEffect(IEffect effect);

    IEffect getEffect();

    /**
     * force - remove without waiting.
     */
    void remove(boolean force);

    /**
     * reset runtime data
     */
    default void reset() {

    }

    /**
     * emit to a given level.
     */
    default void emmit(IEffect effect) {
        emmit(effect, null, null, null);
    }

    default void emmit(IEffect effect, @Nullable Vector3f position, @Nullable Quaternionf rotation, @Nullable Vector3f scale) {
        setEffect(effect);
        if(effect instanceof BlockEffect blockEffect){
            updatePos(new Vector3f(blockEffect.pos.getX()+0.5f,blockEffect.pos.getY()+0.5f,blockEffect.pos.getZ()+0.5f));
        }
        if (position != null) {
            updatePos(position);
        }
        if (rotation != null) {
            updateRotation(rotation);
        }
        if (scale != null) {
            updateScale(scale);
        }
        //setLevel(effect.getLevel());
        if (this instanceof Particle particle) {
            Minecraft.getInstance().particleEngine.add(particle);
        }
    }

    // transform
    default void updatePos(Vector3f newPos) {
        transform().position(newPos);
    }

    default void updateRotation(Quaternionf newRot) {
        transform().rotation(newRot);
    }

    default void updateRotation(Vector3f newRot) {
        transform().rotation(new Quaternionf().rotationXYZ(newRot.x, newRot.y, newRot.z));
    }

    default void updateScale(Vector3f newScale) {
        transform().scale(newScale);
    }

    //default void copyTransformFrom(IFXObject fxObject) {
    //    copyTransformFrom(fxObject, true, true);
    //}
//
    //default void copyTransformFrom(IFXObject fxObject, boolean local, boolean copyParent) {
    //    transform().set(fxObject.transform(), local);
    //    if (copyParent) {
    //        transform().parent(fxObject.transform().parent());
    //    }
    //}
}
