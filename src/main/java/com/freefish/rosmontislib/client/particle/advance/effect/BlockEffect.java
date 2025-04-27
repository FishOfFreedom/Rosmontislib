package com.freefish.rosmontislib.client.particle.advance.effect;

import com.freefish.rosmontislib.client.particle.advance.base.IFXObject;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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
public class BlockEffect extends FXEffect {
    public final Vec3 pos;

    public BlockEffect(Level level, Vec3 pos) {
        super( level);
        this.pos = pos;
    }

    @Override
    public void updateFXObjectTick(IFXObject fxObject) {
    }

    @Override
    public void updateFXObjectFrame(IFXObject fxObject, float partialTicks) {
        fxObject.updatePos(new Vector3f((float) (pos.x + offset.x), (float) (pos.y + offset.y), (float) (pos.z + offset.z)));
    }

    @Override
    public void start() {
    }
}
