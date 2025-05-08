package de.peoples_magic.payloads.spells.handlers;

import de.peoples_magic.Config;
import de.peoples_magic.SpellUtil;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.spells.CastFarmingPayload;
import de.peoples_magic.payloads.spells.CastHastePayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SpellFarmingServerHandler {

    public static void handleDataOnMain(final CastFarmingPayload data, final IPayloadContext context) {
        Player player = context.player();
        Level level = player.level();
        if (!level.isClientSide()) {
            if (player.getData(ModAttachments.FARMING_KNOWLEDGE) > 0) {
                int spell_level = SpellUtil.get_farming_level(player);
                if (!player.getData(ModAttachments.FARMING_IS_ACTIVE)) {
                    // Turn on
                    float spell_cd = player.getData(ModAttachments.HASTE_ACTIVE_CD.get());
                    if (Config.test_mode || spell_cd == 0.0f) {
                        SpellUtil.set_farming_is_active((ServerPlayer) player, true);
                    }
                }
                else {
                    // Turn off
                    SpellUtil.set_farming_is_active((ServerPlayer) player, false);
                    Util.set_cooldown(player, ModAttachments.FARMING_ACTIVE_CD.get(), Util.get_or_last(Config.farming_cds, spell_level));
                }
            }
        }
    }
}
