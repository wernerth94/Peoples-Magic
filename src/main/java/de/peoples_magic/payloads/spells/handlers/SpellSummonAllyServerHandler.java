package de.peoples_magic.payloads.spells.handlers;

import de.peoples_magic.Config;
import de.peoples_magic.SpellUtil;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.entity.spells.*;
import de.peoples_magic.payloads.spells.CastSummonAllyPayload;
import de.peoples_magic.sound.ModSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SpellSummonAllyServerHandler {

    public static void handleDataOnMain(final CastSummonAllyPayload data, final IPayloadContext context) {
        Player player = context.player();
        Level level = player.level();
        if (!level.isClientSide()) {
            int knowledge = player.getData(ModAttachments.SUMMON_ALLY_KNOWLEDGE.get());
            if (knowledge > 0) {
                int spell_level = SpellUtil.get_summon_ally_level(player);
                int player_mana = player.getData(ModAttachments.PLAYER_MANA.get()).intValue();
                int cost = Util.get_or_last(Config.summon_ally_cost, spell_level);
                if (knowledge == 3) {
                    cost *= 2;
                }
                float spell_cd = player.getData(ModAttachments.SUMMON_ALLY_ACTIVE_CD.get());
                if (Config.test_mode || (player_mana >= cost && spell_cd == 0.0f)) {
                    if (knowledge == 2) {
                        Util.set_cooldown(player, ModAttachments.SUMMON_ALLY_ACTIVE_CD.get(), Util.get_or_last(Config.summon_ally_cds, spell_level) / 2);
                    }
                    else if (knowledge == 3) {
                        Util.set_cooldown(player, ModAttachments.SUMMON_ALLY_ACTIVE_CD.get(), Util.get_or_last(Config.summon_ally_cds, spell_level) * 2);
                    }
                    else {
                        Util.set_cooldown(player, ModAttachments.SUMMON_ALLY_ACTIVE_CD.get(), Util.get_or_last(Config.summon_ally_cds, spell_level));
                    }
                    Util.update_player_mana(player, player_mana - cost);
                    SpellUtil.increase_summon_ally_summons((ServerPlayer) player, 1);
                    summon(player, level, spell_level, knowledge);
                    player.level().playSound(null, player.blockPosition(),
                            ModSounds.SUMMON_ALLY.get(), SoundSource.PLAYERS, 1.2f, 1f);
                }
            }
        }
    }

    private static void summon(Player player, Level level, int spell_level, int knowledge) {
        int ticks_to_live = (int)(20 * Util.get_or_last(Config.summon_ally_time_to_live, spell_level));
        if (knowledge == 3) {
            SummonedWarden z = new SummonedWarden(EntityType.WARDEN, level, spell_level);
            z.set_owner(player);
            z.set_ticks_to_live(ticks_to_live);
            z.setPos(player.getPosition(0));
            level.addFreshEntity(z);
        }
        else if (knowledge == 4) {
            SummonedHorse z = new SummonedHorse(EntityType.HORSE, level, spell_level);
            z.set_owner(player);
            z.set_ticks_to_live(ticks_to_live);
            z.setPos(player.getPosition(0));
            level.addFreshEntity(z);
        }
        else {
            switch (spell_level) {
                case 0:
                case 1:
                case 2:
                    SummonedZombie z = new SummonedZombie(EntityType.ZOMBIE, level, spell_level);
                    z.set_owner(player);
                    z.set_ticks_to_live(ticks_to_live);
                    z.setPos(player.getPosition(0));
                    level.addFreshEntity(z);
                    break;
                case 3:
                case 4:
                case 5:
                    SummonedSkeleton skelly = new SummonedSkeleton(EntityType.SKELETON, level, spell_level);
                    skelly.set_owner(player);
                    skelly.set_ticks_to_live(ticks_to_live);
                    skelly.setPos(player.getPosition(0));
                    level.addFreshEntity(skelly);
                    break;
                case 6:
                default:
                    SummonedWitherSkeleton wither_skelly = new SummonedWitherSkeleton(EntityType.WITHER_SKELETON, level);
                    wither_skelly.set_owner(player);
                    wither_skelly.set_ticks_to_live(ticks_to_live);
                    wither_skelly.setPos(player.getPosition(0));
                    level.addFreshEntity(wither_skelly);
                    break;
            }
        }
    }
}
