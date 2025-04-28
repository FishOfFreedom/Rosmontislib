package com.freefish.rosmontislib.commom.init;

import com.freefish.rosmontislib.commom.init.generator.RLResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class DamageSourceHandle {
    public static DamageSource realDamage(Entity snowMonster) {
        return new DamageSource(snowMonster.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).
                getHolderOrThrow(RLResourceKey.REAL_DAMAGE), snowMonster);
    }
}
