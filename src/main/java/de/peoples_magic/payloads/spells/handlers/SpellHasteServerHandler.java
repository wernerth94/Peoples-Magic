package de.peoples_magic.payloads.spells.handlers;

import de.peoples_magic.Config;
import de.peoples_magic.SpellUtil;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.spells.CastHastePayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SpellHasteServerHandler {

    public static void handleDataOnMain(final CastHastePayload data, final IPayloadContext context) {
        Player player = context.player();
        Level level = player.level();
        if (!level.isClientSide()) {
            if (player.getData(ModAttachments.HASTE_KNOWLEDGE) > 0) {
                int spell_level = SpellUtil.get_haste_level(player);
                if (!player.getData(ModAttachments.HASTE_IS_ACTIVE)) {
                    // Turn on
                    int player_mana = player.getData(ModAttachments.PLAYER_MANA.get()).intValue();
                    float spell_cd = player.getData(ModAttachments.HASTE_ACTIVE_CD.get());
                    if (Config.test_mode || (player_mana >= 1 && spell_cd == 0.0f)) {
                        SpellUtil.set_haste_is_active((ServerPlayer) player, true);
                        player.setData(ModAttachments.HASTE_LAST_REFRESHED, 1000); // turn effect on in the same server tick
                    }
                    else {
                        SpellUtil.spell_fail_sound(player);
                        SpellUtil.spell_fail_indicators((ServerPlayer) player, "haste", player_mana >= 1, spell_cd == 0.0f);
                    }
                }
                else {
                    // Turn off
                    SpellUtil.set_haste_is_active((ServerPlayer) player, false);
                    Util.set_cooldown(player, ModAttachments.HASTE_ACTIVE_CD.get(), Util.get_or_last(Config.haste_cds, spell_level));
                }
            }
        }
    }
}
