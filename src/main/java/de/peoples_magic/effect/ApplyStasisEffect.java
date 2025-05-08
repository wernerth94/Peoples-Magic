package de.peoples_magic.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ApplyStasisEffect extends MobEffect {
    protected ApplyStasisEffect(int color) {
        super(MobEffectCategory.HARMFUL, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        BlockPos pos = entity.blockPosition();
        int floating_height = 0;
        while (level.getBlockState(pos.below(floating_height)).isAir()) {
            floating_height++;
        }

        Vec3 delta = entity.getDeltaMovement();
        if (delta.y < 0) {
            delta = delta.multiply(0, 0.1, 0);
        }
        entity.setDeltaMovement(delta);
        if (floating_height < 2) {
            entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 3, 1,
                    false, false, false));
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
