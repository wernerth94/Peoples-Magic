package de.peoples_magic.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class StasisEffect extends MobEffect {
    protected StasisEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }
}
