package de.peoples_magic.potion;

import de.peoples_magic.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class ManaPotion extends Potion {

    public ManaPotion() {
        super("Mana Potion", new MobEffectInstance(ModEffects.MANA_POTION_EFFECT, 0, 0));
    }
}
