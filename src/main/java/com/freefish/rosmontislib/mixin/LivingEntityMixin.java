package com.freefish.rosmontislib.mixin;

import com.freefish.rosmontislib.commom.init.generator.RLResourceKey;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow
    protected abstract void hurtArmor(DamageSource pDamageSource, float pDamageAmount);

    @Shadow
    private static EntityDataAccessor<Float> DATA_HEALTH_ID;

    @ModifyVariable(
            method = "hurt",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0
    )
    private float onHurt(float amount, DamageSource source) {
        if(source.is(RLResourceKey.REAL_DAMAGE)){
            LivingEntity entity = (LivingEntity) (Object) this;
            if(entity.level()!=null&&!(entity instanceof Player player&&player.isCreative())&&!entity.level().isClientSide){

                if (amount <= 0) return 0;
                amount = this.getDamageAfterArmorAbsorb(source, amount,entity);
                amount = this.getDamageAfterMagicAbsorb(source, amount,entity);
                float f1 = Math.max(amount - entity.getAbsorptionAmount(), 0.0F);
                entity.setAbsorptionAmount(entity.getAbsorptionAmount() - (amount - f1));

                if (f1 != 0.0F) {
                    entity.getCombatTracker().recordDamage(source, f1);

                    float currentHealth = entity.getEntityData().get(DATA_HEALTH_ID);
                    entity.getEntityData().set(DATA_HEALTH_ID, currentHealth - f1);

                    entity.setAbsorptionAmount(entity.getAbsorptionAmount() - f1);
                    this.gameEvent(GameEvent.ENTITY_DAMAGE);
                }
                return 1;
            }
        }
        return amount;
    }

    private float getDamageAfterArmorAbsorb(DamageSource pDamageSource, float pDamageAmount,LivingEntity living) {
        this.hurtArmor(pDamageSource, pDamageAmount);
        pDamageAmount = CombatRules.getDamageAfterAbsorb(pDamageAmount, (float)living.getArmorValue(), (float)living.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
        return pDamageAmount;
    }

    private float getDamageAfterMagicAbsorb(DamageSource pDamageSource, float pDamageAmount,LivingEntity living) {
            if (living.hasEffect(MobEffects.DAMAGE_RESISTANCE) && !pDamageSource.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
                int i = (living.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() + 1) * 5;
                int j = 25 - i;
                float f = pDamageAmount * (float)j;
                pDamageAmount = Math.max(f / 25.0F, 0.0F);
            }

            if (pDamageAmount <= 0.0F) {
                return 0.0F;
            } else if (pDamageSource.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
                return pDamageAmount;
            } else {
                int k = EnchantmentHelper.getDamageProtection(this.getArmorSlots(), pDamageSource);
                if (k > 0) {
                    pDamageAmount = CombatRules.getDamageAfterMagicAbsorb(pDamageAmount, (float)k);
                }
                return pDamageAmount;
            }
    }
}