package de.peoples_magic.payloads.spells.handlers;

import de.peoples_magic.Config;
import de.peoples_magic.SpellUtil;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.entity.spells.AetherGripProjectile;
import de.peoples_magic.entity.ModEntities;
import de.peoples_magic.payloads.spells.CastAetherGripPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SpellAetherGripServerHandler {
    public static void handleDataOnMain(final CastAetherGripPayload data, final IPayloadContext context) {
        Player player = context.player();
        Level level = player.level();
        if (player.level() instanceof ServerLevel server_level) {
            if (player.getData(ModAttachments.AETHER_GRIP_KNOWLEDGE) > 0) {
                int active_entity_id = player.getData(ModAttachments.AETHER_GRIP_ACTIVE_ENTITY);
                sanity_check_id(player.level(), active_entity_id);
                if (active_entity_id > 0) {
                    AetherGripProjectile projectile = (AetherGripProjectile) player.level().getEntity(active_entity_id);
                    if (projectile != null) {
                        projectile.reverse();
                    }
                    SpellUtil.set_aether_grip_entity((ServerPlayer) player, null);
                }
                else {
                    int player_mana = player.getData(ModAttachments.PLAYER_MANA.get()).intValue();
                    float spell_cd = player.getData(ModAttachments.AETHER_GRIP_ACTIVE_CD.get());
                    int spell_level = SpellUtil.get_aether_grip_level(player);
                    int cost = Util.get_or_last(Config.aether_grip_cost, spell_level);
                    if (Config.test_mode || (player_mana >= cost && spell_cd == 0.0f)) {
                        AetherGripProjectile projectile = ModEntities.AETHER_GRIP_PROJECTILE.get().create(server_level, EntitySpawnReason.TRIGGERED);
                        projectile.as_projectile(player, spell_level, player.getX(), player.getEyeY() - 0.3f, player.getZ());
                        projectile.shootFromRotation(player.getXRot(), player.getYRot(), 0.0F);
                        level.gameEvent(GameEvent.PROJECTILE_SHOOT, projectile.position(), GameEvent.Context.of(player));
                        level.addFreshEntity(projectile);

                        Util.update_player_mana(player, player_mana - cost);
                        Util.set_cooldown(player, ModAttachments.AETHER_GRIP_ACTIVE_CD.get(), Util.get_or_last(Config.aether_grip_cds, spell_level));
                        player.level().playSound(null, player.blockPosition(),
                                SoundEvents.TRIDENT_THROW.value(), SoundSource.PLAYERS, 0.6f, 0.1f);
                    }
                }
            }
        }
    }

    private static int sanity_check_id(Level level, int id) {
        try {
            AetherGripProjectile projectile = (AetherGripProjectile) level.getEntity(id);
            return id;
        } catch (Exception e) {
            return -1;
        }
    }
}
