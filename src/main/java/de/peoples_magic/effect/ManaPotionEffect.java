package de.peoples_magic.effect;

import de.peoples_magic.Config;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.attributes.ModAttributes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ManaPotionEffect extends MobEffect {
    public ManaPotionEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }


    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player player) {
            double player_mana = player.getData(ModAttachments.PLAYER_MANA);
            if (player_mana < player.getAttributeValue(ModAttributes.MAX_MANA)) {
                Util.update_player_mana(player, player_mana + Config.mana_potion_mana_amount);
            }
        }
        return super.applyEffectTick(level, livingEntity, amplifier);
    }

    @Override
    public boolean isInstantenous() {
        return true;
    }
}
