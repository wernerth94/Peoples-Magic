package de.peoples_magic.entity.spells;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;

public class NotSummonedSelector implements TargetingConditions.Selector {

    @Override
    public boolean test(LivingEntity livingEntity, ServerLevel serverLevel) {
        return livingEntity instanceof Monster && !(livingEntity instanceof SummonedEntity);
    }
}