package de.peoples_magic.payloads.spells.handlers;

import de.peoples_magic.Config;
import de.peoples_magic.SpellUtil;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.effect.ModEffects;
import de.peoples_magic.payloads.spells.CastAbsorptionPayload;
import de.peoples_magic.payloads.spells.CastRepelPayload;
import de.peoples_magic.sound.ModSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SpellRepelServerHandler {

    public static void handleDataOnMain(final CastRepelPayload data, final IPayloadContext context) {
        Player player = context.player();
        if (!player.level().isClientSide) {
            int knowledge = player.getData(ModAttachments.REPEL_KNOWLEDGE);
            if (knowledge > 0) {
                int player_mana = player.getData(ModAttachments.PLAYER_MANA.get()).intValue();
                float spell_cd = player.getData(ModAttachments.REPEL_ACTIVE_CD.get());
                int spell_level = SpellUtil.get_repel_level(player);
                int cost = Util.get_or_last(Config.repel_cost, spell_level);
                if (knowledge == 3) {
                    // lightning storm
                    cost = (int) (cost * 1.5);
                }
                if (Config.test_mode || (player_mana >= cost && spell_cd == 0.0f)) {
                    float duration_sec = Util.get_or_last(Config.repel_durations, spell_level);
                    player.addEffect(new MobEffectInstance(ModEffects.REPEL_EFFECT, (int)(duration_sec * 20)));
                    Util.update_player_mana(player, player_mana - cost);
                    Util.set_cooldown(player, ModAttachments.REPEL_ACTIVE_CD.get(), Util.get_or_last(Config.repel_cds, spell_level));
                    player.setData(ModAttachments.REPEL_CAST, System.currentTimeMillis());
                    player.level().playSound(null, player.blockPosition(),
                            SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.PLAYERS, 0.6f, 0.7f);
                    if (knowledge == 2) {
                        // statis
                        context.player().addEffect(new MobEffectInstance(ModEffects.STASIS_EFFECT, (int)(duration_sec * 20f), 1,
                                false, false, true));
                    }
                    else if (knowledge == 3) {
                        // lightning storm
                        context.player().addEffect(new MobEffectInstance(ModEffects.LIGHTNING_STORM_EFFECT, (int)(duration_sec * 20f), 1,
                                false, false, true));
                    }
                }
                else {
                    SpellUtil.spell_fail_sound(player);
                    SpellUtil.spell_fail_indicators((ServerPlayer) player, "repel", player_mana >= cost, spell_cd == 0.0f);
                }
            }
        }
    }
}
