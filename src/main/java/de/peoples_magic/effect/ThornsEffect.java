package de.peoples_magic.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;


public class ThornsEffect extends MobEffect {

    protected ThornsEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }

    public void onMobHurt(ServerLevel server_level, LivingEntity entity, int p_338438_, DamageSource damage_source, float p_338367_) {
        if (damage_source.isDirect()) {
            damage_source.getEntity().hurtServer(server_level, server_level.damageSources().mobAttack(entity), 8.0f);
        }
    }
}
