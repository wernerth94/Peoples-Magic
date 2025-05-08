package de.peoples_magic.payloads.spells.handlers;

import de.peoples_magic.Config;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.entity.spells.FireballProjectile;
import de.peoples_magic.payloads.spells.CastFireballPayload;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SpellFireballServerHandler {

    public static void handleDataOnMain(final CastFireballPayload data, final IPayloadContext context) {
        Player player = context.player();
        Level level = player.level();
        if (!level.isClientSide()) {
            int knowledge = player.getData(ModAttachments.FIREBALL_KNOWLEDGE);
            if (knowledge > 0) {
                int spell_level = Util.level_from_progression(Config.fireball_progression, player.getData(ModAttachments.FIREBALL_HITS));
                int player_mana = player.getData(ModAttachments.PLAYER_MANA.get()).intValue();
                float spell_cd = player.getData(ModAttachments.FIREBALL_ACTIVE_CD.get());
                int cost = Util.get_or_last(Config.absorption_cost, spell_level);
                if (Config.test_mode || (player_mana >= cost && spell_cd == 0.0f)) {
                    FireballProjectile projectile = new FireballProjectile(player, level, spell_level, 20 * 10);

                    projectile.shootFromRotation(projectile, player.getXRot(), player.getYRot(), 0.0F, 1.0f, 0);
                    level.addFreshEntity(projectile);

                    Util.update_player_mana(player, Integer.valueOf(player_mana - cost));
                    if (knowledge == 4) {
                        Util.set_cooldown(player, ModAttachments.FIREBALL_ACTIVE_CD.get(), Util.get_or_last(Config.fireball_cds, spell_level) / 2);
                    }
                    else {
                        Util.set_cooldown(player, ModAttachments.FIREBALL_ACTIVE_CD.get(), Util.get_or_last(Config.fireball_cds, spell_level));
                    }
                    level.playSound(null, player.position().x, player.position().y, player.position().z,
                            SoundEvents.WIND_CHARGE_BURST, SoundSource.PLAYERS, 0.5f, 0.8f);
                }
            }
        }
    }
}
