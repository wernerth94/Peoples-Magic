package de.peoples_magic.payloads.spells.handlers;

import de.peoples_magic.Config;
import de.peoples_magic.SpellUtil;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.effect.ModEffects;
import de.peoples_magic.entity.spells.SummonedEntity;
import de.peoples_magic.payloads.spells.CastIceconePayload;
import de.peoples_magic.sound.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public class SpellIceConeServerHandler {

    public static void handleDataOnMain(final CastIceconePayload data, final IPayloadContext context) {
        Player player = context.player();
        if (player.level() instanceof ServerLevel level) {
            int knowledge = player.getData(ModAttachments.ICE_CONE_KNOWLEDGE);
            if (knowledge > 0) {
                int player_mana = player.getData(ModAttachments.PLAYER_MANA.get()).intValue();
                float spell_cd = player.getData(ModAttachments.ICE_CONE_ACTIVE_CD.get());
                int spell_level = SpellUtil.get_ice_cone_level(player);
                int cost = Util.get_or_last(Config.ice_cone_cost, spell_level);
                if (Config.test_mode || (player_mana >= cost && spell_cd == 0.0f)) {
                    if (knowledge == 3) {
                        shoot_ring(player, level, knowledge);
                    }
                    else
                    {
                        shoot_cone(player, level, knowledge);
                    }

                    Util.update_player_mana(player, player_mana - cost);
                    Util.set_cooldown(player, ModAttachments.ICE_CONE_ACTIVE_CD.get(), Util.get_or_last(Config.ice_cone_cds, spell_level));
                    level.playSound(null, player.blockPosition(), ModSounds.ICE_CONE_CAST.get(), SoundSource.PLAYERS, 1f, 0.7f);
                }
                else {
                    SpellUtil.spell_fail_sound(player);
                    SpellUtil.spell_fail_indicators((ServerPlayer) player, "ice_cone", player_mana >= cost, spell_cd == 0.0f);
                }
            }
        }
    }

    private static void shoot_cone(Player player, ServerLevel level, int knowledge) {
        Vec3 direction = Util.angles_to_direction( player.getXRot(), player.getYRot(), 0);
        for (float mult = 2f; mult <= 10f; mult += 1f) {
            Vec3 point = player.position().add(0, 0.8, 0).add(direction.scale(mult));
            level.sendParticles(ParticleTypes.SNOWFLAKE, point.x, point.y, point.z, 7,
                    0.3*mult, 0.3*mult, 0.3*mult, 0);
        }

        List<Vec3> pois = new ArrayList<>();
        for (float mult = 1f; mult <= 10f; mult += 2f) {
            pois.add(player.position().add(0, 0.8, 0).add(direction.scale(mult)));
        }
        int entity_hit_count = 0;
        for (LivingEntity entity : level.getEntitiesOfClass(Monster.class, player.getBoundingBox().inflate(10))) {
            if (!(entity instanceof SummonedEntity)) {
                if (entity_hit(entity, pois)) {
                    do_hit_entity(entity, level, (ServerPlayer) player, knowledge);
                    entity_hit_count++;
                    if (entity_hit_count == 2) {
                        SpellUtil.increase_ice_cone_progression((ServerPlayer) player, 1);
                    }
                }
            }
        }
    }


    private static void shoot_ring(Player player, ServerLevel level, int knowledge) {
        List<Vec3> directions = new ArrayList<>();
        for (int i = 0; i < 360; i += 20) {
            directions.add(Util.angles_to_direction( 0, i, 0));
        }
        for (float mult = 2f; mult <= 10f; mult += 1f) {
            for (Vec3 direction : directions) {
                Vec3 point = player.position().add(0, 0.8, 0).add(direction.scale(mult));
                level.sendParticles(ParticleTypes.SNOWFLAKE, point.x, point.y, point.z, 4,
                        0.3, 0.3, 0.3, 0);
            }
        }

        int entity_hit_count = 0;
        for (LivingEntity entity : level.getEntitiesOfClass(Monster.class, player.getBoundingBox().inflate(10))) {
            if (!(entity instanceof SummonedEntity)) {
                if (entity.position().distanceTo(player.position()) <= 10f) {
                    do_hit_entity(entity, level, (ServerPlayer) player, knowledge);
                    entity_hit_count++;
                    if (entity_hit_count == 2) {
                        SpellUtil.increase_ice_cone_progression((ServerPlayer) player, 1);
                    }
                }
            }
        }
    }


    private static void do_hit_entity(LivingEntity entity, ServerLevel level, ServerPlayer player, int knowledge) {
        int spell_level = SpellUtil.get_ice_cone_level(player);
        entity.hurtServer(level, level.damageSources().magic(), Util.get_or_last(Config.ice_cone_damages, spell_level));
        if (knowledge == 2) {
            entity.addEffect(new MobEffectInstance(
                    ModEffects.ABSOLUTE_ZERO_EFFECT,
                    (int) (Util.get_or_last(Config.ice_cone_durations, spell_level) * 20f),
                    0
            ));
        }
        else {
            entity.addEffect(new MobEffectInstance(
                    MobEffects.SLOWNESS,
                    (int) (Util.get_or_last(Config.ice_cone_durations, spell_level) * 20f),
                    Util.get_or_last(Config.ice_cone_amplifiers, spell_level)
            ));
        }
    }

    private static boolean entity_hit(LivingEntity entity, List<Vec3> pois) {
        Vec3 ent_pos = entity.position();
        double last_dist = 99999F;
        for (int i = 0; i < pois.size(); i++) {
            Vec3 poi = pois.get(i);
            double dist = ent_pos.distanceTo(poi);
            if (dist <= 1.3 * i) {
                return true;
            }
            if (dist > last_dist) {
                break;
            }
            last_dist = dist;
        }
        return false;
    }
}
