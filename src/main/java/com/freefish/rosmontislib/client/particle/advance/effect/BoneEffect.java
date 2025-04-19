package com.freefish.rosmontislib.client.particle.advance.effect;

import com.freefish.rosmontislib.client.particle.advance.base.IFXObject;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author KilaBash
 * @date 2023/6/5
 * @implNote EntityEffect
 */
@OnlyIn(Dist.CLIENT)
public class BoneEffect extends FXEffect {
    public static Map<Entity, List<BoneEffect>> CACHE = new HashMap<>();
    public final Entity entity;

    public BoneEffect(Level level, Entity entity) {
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
