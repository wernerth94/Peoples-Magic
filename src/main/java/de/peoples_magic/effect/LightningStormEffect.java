package de.peoples_magic.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class LightningStormEffect extends MobEffect {
    protected LightningStormEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }
}
