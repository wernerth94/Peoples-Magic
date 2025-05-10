package de.peoples_magic.payloads.spells.handlers;

import de.peoples_magic.Config;
import de.peoples_magic.SpellUtil;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.effect.ModEffects;
import de.peoples_magic.payloads.spells.CastAbsorptionPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SpellAbsorptionServerHandler {

    public static void handleDataOnMain(final CastAbsorptionPayload data, final IPayloadContext context) {
        Player player = context.player();
        if (!player.level().isClientSide) {
            int knowledge = player.getData(ModAttachments.ABSORPTION_KNOWLEDGE);
            if (knowledge > 0) {
                int player_mana = player.getData(ModAttachments.PLAYER_MANA.get()).intValue();
                float spell_cd = player.getData(ModAttachments.ABSORPTION_ACTIVE_CD.get());
                int spell_level = Util.level_from_progression(Config.absorption_progression, player.getData(ModAttachments.ABSORPTION_MITIGATION));
                int cost = Util.get_or_last(Config.absorption_cost, spell_level);
                if (Config.test_mode || (player_mana >= cost && spell_cd == 0.0f)) {

                    float duration_s = Util.get_or_last(Config.absorption_durations, spell_level);
                    int amplifier = Util.get_or_last(Config.absorption_amplifiers, spell_level);
                    context.player().addEffect(new MobEffectInstance(MobEffects.ABSORPTION, (int)(duration_s * 20f), amplifier,
                            false, false, false));
                    if (knowledge == 3) {
                        context.player().addEffect(new MobEffectInstance(ModEffects.THORNS_EFFECT, (int)(duration_s * 1.5 * 20f), 1,
                                false, false, true));
                    } else if (knowledge == 4) {
                        context.player().addEffect(new MobEffectInstance(MobEffects.SLOWNESS, (int)(duration_s * 1.5 * 20f), 1,
                                false, false, true));
                    }

                    Util.update_player_mana(player, Integer.valueOf(player_mana - cost));
                    if (knowledge == 2) {
                        // CD expertise
                        Util.set_cooldown(player, ModAttachments.ABSORPTION_ACTIVE_CD.get(), Util.get_or_last(Config.absorption_cds, spell_level) / 2);
                    }
                    else {
                        Util.set_cooldown(player, ModAttachments.ABSORPTION_ACTIVE_CD.get(), Util.get_or_last(Config.absorption_cds, spell_level));
                    }
                    player.setData(ModAttachments.LAST_ABSORPTION_CAST, System.currentTimeMillis());
                    player.level().playSound(null, player.blockPosition(),
                            SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 0.5f, 1.9f);
                }
                else {
                    SpellUtil.spell_fail_sound(player);
                    SpellUtil.spell_fail_indicators((ServerPlayer) player, "absorption", player_mana >= cost, spell_cd == 0.0f);
                }
            }
        }
    }
}
