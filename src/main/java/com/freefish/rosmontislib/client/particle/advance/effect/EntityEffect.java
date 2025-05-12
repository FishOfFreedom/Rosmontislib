package com.freefish.rosmontislib.client.particle.advance.effect;

import com.freefish.rosmontislib.client.particle.advance.base.IFXObject;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author KilaBash
 * @date 2023/6/5
 * @implNote EntityEffect
 */
public class EntityEffect extends FXEffect {
    public static Map<Entity, List<EntityEffect>> CACHE = new HashMap<>();
    public final Entity entity;

    public EntityEffect(Level level, Entity entity) {
        super(level);
        this.entity = entity;
    }

    @Override
    public void updateFXObjectTick(IFXObject fxObject) {
    }

    @Override
    public void updateFXObjectFrame(IFXObject fxObject, float partialTicks) {
        var position = entity.getPosition(partialTicks);
        fxObject.updatePos(new Vector3f((float) (position.x + offset.x), (float) (position.y + offset.y), (float) (position.z + offset.z)));
    }

    @Override
    public void start() {

    }
}
