package de.peoples_magic;

import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.entity.spells.*;
import de.peoples_magic.overlays.FadingMessageOverlay;
import de.peoples_magic.payloads.sync.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static de.peoples_magic.Util.level_from_progression;
import static de.peoples_magic.attachments.ModAttachments.*;

public class SpellUtil {
    private static final List<Tuple<Vec3, List<Monster>>> active_fireball_voids = new ArrayList<>();
    private static final List<Integer> active_fireball_voids_tick_count = new ArrayList<>();

    public static void add_active_fireball_void(Vec3 pos, List<Monster> void_entities) {
        active_fireball_voids.add(new Tuple<>(pos, void_entities));
        active_fireball_voids_tick_count.add(0);
    }

    public static void tick_active_fireball_voids(ServerLevel server_level) {
        for (int i = active_fireball_voids.size()-1; i >= 0; i--) {
            Tuple<Vec3, List<Monster>> current_void = active_fireball_voids.get(i);
            Vec3 pos = current_void.getA();
            for (LivingEntity entity : current_void.getB()) {
                Vec3 direction = pos.subtract(entity.position()).normalize();
                entity.move(MoverType.PLAYER, direction.scale(0.2f));
                entity.hurtServer(server_level, server_level.damageSources().magic(), 2.5f);
            }
            int current_tick_count = active_fireball_voids_tick_count.get(i);
            if (current_tick_count == 15 || current_tick_count == 30 || current_tick_count == 45)
            {
                server_level.sendParticles(ParticleTypes.SONIC_BOOM, pos.x, pos.y, pos.z, 1, 0.3, 0.3, 0.3, 0);
            }

            if (current_tick_count >= 20 * 3)
            {
                active_fireball_voids_tick_count.remove(i);
                active_fireball_voids.remove(i);
            }
            else
            {
                active_fireball_voids_tick_count.set(i, current_tick_count + 1);
            }
        }
    }


    private static final Map<ServerPlayer, Vec3> old_view_vectors = new HashMap<>();
    private static final Map<ServerPlayer, Boolean> view_vectors_updates = new HashMap<>();

    public static void check_homing_missile(ServerPlayer player) {
        int active_entity_id = player.getData(AETHER_GRIP_ACTIVE_ENTITY);
        if (active_entity_id > 0 && player.getData(AETHER_GRIP_KNOWLEDGE) == 3) {
            if (old_view_vectors.containsKey(player)) {
                Vec3 diff = player.getViewVector(1f).subtract(old_view_vectors.get(player));
                AetherGripProjectile projectile = (AetherGripProjectile) player.level().getEntity(active_entity_id);
                if (projectile != null && !projectile.already_reversed) {
                    if (diff.length() > 0.01) {
                        projectile.reversal_point = projectile.reversal_point.add(diff.scale(20));
                    }
                    view_vectors_updates.put(player, true);
                }
                else {
                    view_vectors_updates.put(player, false);
                    SpellUtil.set_aether_grip_entity(player, null);
                }
            }
            old_view_vectors.put(player, player.getViewVector(1f));
            view_vectors_updates.put(player, true);
        }
    }

    public static void clean_homing_missiles() {
        for (Player key : old_view_vectors.keySet()) {
            if (!view_vectors_updates.getOrDefault(key, false)) {
                old_view_vectors.remove(key);
            }
        }
        view_vectors_updates.clear();
    }

    public static void increase_absorption_mitigation(ServerPlayer player, float amount) {
        float old_amount = player.getData(ABSORPTION_MITIGATION);
        int old_level = level_from_progression(Config.absorption_progression, old_amount);
        float new_amount = old_amount + amount;
        int new_level = level_from_progression(Config.absorption_progression, new_amount);
        player.setData(ABSORPTION_MITIGATION, new_amount);
        PacketDistributor.sendToPlayer(player, new AbsorptionMitigationPayload(new_amount));
        if (new_level > old_level) {
            PacketDistributor.sendToPlayer(player, new DisplayMessagePayload(String.format("Absorption is now level %d", new_level), 4000));
            player.level().playSound(null, player.position().x, player.position().y, player.position().z,
                    SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1f, 0.8f);
        }
    }

    public static void increase_repel_spiders(ServerPlayer player, int amount) {
        int old_amount = player.getData(REPEL_SPIDERS_REPELLED);
        int old_level = level_from_progression(Config.repel_progression, old_amount);
        int new_amount = old_amount + amount;
        int new_level = level_from_progression(Config.repel_progression, new_amount);
        player.setData(REPEL_SPIDERS_REPELLED, new_amount);
        PacketDistributor.sendToPlayer(player, new RepelSpidersPayload(new_amount));
        if (new_level > old_level) {
            PacketDistributor.sendToPlayer(player, new DisplayMessagePayload(String.format("Repel is now level %d", new_level), 4000));
            player.level().playSound(null, player.position().x, player.position().y, player.position().z,
                    SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1f, 0.8f);
        }
    }

    public static void increase_fireball_hit(ServerPlayer player, int amount) {
        int old_amount = player.getData(FIREBALL_HITS);
        int old_level = level_from_progression(Config.fireball_progression, old_amount);
        int new_amount = old_amount + amount;
        int new_level = level_from_progression(Config.fireball_progression, new_amount);
        player.setData(FIREBALL_HITS, new_amount);
        PacketDistributor.sendToPlayer(player, new FireballHitPayload(new_amount));
        if (new_level > old_level) {
            PacketDistributor.sendToPlayer(player, new DisplayMessagePayload(String.format("Fireball is now level %d", new_level), 4000));
            player.level().playSound(null, player.position().x, player.position().y, player.position().z,
                    SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1f, 0.8f);
        }
    }

    public static void increase_aether_grip_pulls(ServerPlayer player, int amount) {
        int old_amount = player.getData(AETHER_GRIP_PULLS);
        int old_level = level_from_progression(Config.aether_grip_progression, old_amount);
        int new_amount = old_amount + amount;
        int new_level = level_from_progression(Config.aether_grip_progression, new_amount);
        player.setData(AETHER_GRIP_PULLS, new_amount);
        PacketDistributor.sendToPlayer(player, new AetherGripPullsPayload(new_amount));
        if (new_level > old_level) {
            PacketDistributor.sendToPlayer(player, new DisplayMessagePayload(String.format("Aether Grip is now level %d", new_level), 4000));
            player.level().playSound(null, player.position().x, player.position().y, player.position().z,
                    SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1f, 0.8f);
        }
    }

    public static void increase_ice_cone_progression(ServerPlayer player, int amount) {
        int old_amount = player.getData(ICE_CONE_MULTIPLE_HIT);
        int old_level = level_from_progression(Config.ice_cone_progression, old_amount);
        int new_amount = old_amount + amount;
        int new_level = level_from_progression(Config.ice_cone_progression, new_amount);
        player.setData(ICE_CONE_MULTIPLE_HIT, new_amount);
        PacketDistributor.sendToPlayer(player, new IceConeHitsPayload(new_amount));
        if (new_level > old_level) {
            PacketDistributor.sendToPlayer(player, new DisplayMessagePayload(String.format("Ice Cone is now level %d", new_level), 4000));
            player.level().playSound(null, player.position().x, player.position().y, player.position().z,
                    SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1f, 0.8f);
        }
    }

    public static void increase_haste_uptime(ServerPlayer player, float amount) {
        float old_amount = player.getData(HASTE_UPTIME);
        int old_level = level_from_progression(Config.haste_progression, (int)old_amount/20f);
        float new_amount = old_amount + amount;
        int new_level = level_from_progression(Config.haste_progression, (int)new_amount/20f);
        player.setData(HASTE_UPTIME, new_amount);
        PacketDistributor.sendToPlayer(player, new HasteUptimePayload(new_amount));
        if (new_level > old_level) {
            PacketDistributor.sendToPlayer(player, new DisplayMessagePayload(String.format("Haste is now level %d", new_level), 4000));
            player.level().playSound(null, player.position().x, player.position().y, player.position().z,
                    SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1f, 0.8f);
        }
    }

    public static void increase_summon_ally_summons(ServerPlayer player, int amount) {
        int old_amount = player.getData(SUMMON_ALLY_SUMMONS);
        int old_level = level_from_progression(Config.summon_ally_progression, old_amount);
        int new_amount = old_amount + amount;
        int new_level = level_from_progression(Config.summon_ally_progression, new_amount);
        player.setData(SUMMON_ALLY_SUMMONS, new_amount);
        PacketDistributor.sendToPlayer(player, new SummonAllySummonsPayload(new_amount));
        if (new_level > old_level) {
            PacketDistributor.sendToPlayer(player, new DisplayMessagePayload(String.format("Summon Ally is now level %d", new_level), 4000));
            player.level().playSound(null, player.position().x, player.position().y, player.position().z,
                    SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1f, 0.8f);
        }
    }

    public static void set_haste_is_active(ServerPlayer player, boolean is_active) {
        player.setData(HASTE_IS_ACTIVE, is_active);
        PacketDistributor.sendToPlayer(player, new HasteIsActivePayload(is_active));
    }

    public static void increase_animals_bred(ServerPlayer player, int amount) {
        int old_amount = player.getData(FARMING_ANIMALS_BRED);
        int old_level = level_from_progression(Config.farming_progression, old_amount);
        int new_amount = old_amount + amount;
        int new_level = level_from_progression(Config.farming_progression, new_amount);
        player.setData(FARMING_ANIMALS_BRED, new_amount);
        PacketDistributor.sendToPlayer(player, new FarmingAnimalsBredPayload(new_amount));
        if (new_level > old_level) {
            PacketDistributor.sendToPlayer(player, new DisplayMessagePayload(String.format("Farming is now level %d", new_level), 4000));
            player.level().playSound(null, player.position().x, player.position().y, player.position().z,
                    SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1f, 0.8f);
        }
    }

    public static void set_farming_is_active(ServerPlayer player, boolean is_active) {
        player.setData(FARMING_IS_ACTIVE, is_active);
        PacketDistributor.sendToPlayer(player, new FarmingIsActivePayload(is_active));
    }

    public static void set_aether_grip_entity(ServerPlayer player, @Nullable Entity entity) {
        int id;
        if (entity == null) {
            id = -1;
        }
        else {
            id =  entity.getId();
        }
        player.setData(AETHER_GRIP_ACTIVE_ENTITY, id);
        PacketDistributor.sendToPlayer(player, new AetherGripEntityPayload(id));
    }

    public static void tick_haste(ServerPlayer player) {
        int knowledge = player.getData(ModAttachments.HASTE_KNOWLEDGE);
        if (knowledge > 0 && player.getData(HASTE_IS_ACTIVE)) {
            double player_mana = player.getData(PLAYER_MANA);
            int spell_level = SpellUtil.get_haste_level(player);
            float cost = Util.get_or_last(Config.haste_cost_per_s, spell_level) / 20f;
            if (Config.test_mode || player_mana > cost) {
                int frequency = 10;
                int last_refreshed = player.getData(HASTE_LAST_REFRESHED);
                int amp = Util.get_or_last(Config.haste_amplifiers, spell_level);
                if (last_refreshed >= frequency) {
                    int move_amp = knowledge == 2 ? amp + 1 : amp;
                    int dig_amp = knowledge == 3 ? amp + 1 : amp;
                    player.addEffect(new MobEffectInstance(MobEffects.HASTE, frequency+2, dig_amp, false, false));
                    player.addEffect(new MobEffectInstance(MobEffects.SPEED, frequency+2, move_amp, false, false));
                    player.setData(HASTE_LAST_REFRESHED, 0);
                    SpellUtil.increase_haste_uptime(player, frequency);
                } else {
                    player.setData(HASTE_LAST_REFRESHED, last_refreshed + 1);
                }
                Util.update_player_mana(player, player_mana - cost);
            }
            else {
                SpellUtil.set_haste_is_active(player, false);
                Util.set_cooldown(player, ModAttachments.HASTE_ACTIVE_CD.get(), Util.get_or_last(Config.haste_cds, spell_level));
            }
        }
    }

    public static void check_haste_fortune(ServerPlayer player, BlockState block_state) {
        if (is_ore(block_state)) {
            int knowledge = player.getData(ModAttachments.HASTE_KNOWLEDGE);
            if (knowledge == 3) {
                List<ItemStack> drops = Block.getDrops(block_state, (ServerLevel)player.level(), player.blockPosition(), null,
                    player, player.getItemInHand(player.getUsedItemHand()));
                for (ItemStack drop : drops) {
                    drop.setCount(1);
                    Vec3 view_vec = player.getViewVector(0.5f);
                    ItemEntity item_entity = new ItemEntity(player.level(),
                            player.getX() + view_vec.x * 2,
                            player.getEyeY() + view_vec.y * 2,
                            player.getZ() + view_vec.z * 2, drop);
                    player.level().addFreshEntity(item_entity);
                }
            }
        }
    }

    private static boolean is_ore(BlockState block_state) {
        // early out
        if (block_state.is(Blocks.STONE) ||
                block_state.is(Blocks.DIRT) ||
                block_state.is(Blocks.GRASS_BLOCK)) {
            return false;
        }
        return block_state.is(BlockTags.COAL_ORES) ||
                block_state.is(BlockTags.REDSTONE_ORES) ||
                block_state.is(BlockTags.COPPER_ORES) ||
                block_state.is(BlockTags.IRON_ORES) ||
                block_state.is(BlockTags.GOLD_ORES) ||
                block_state.is(BlockTags.DIAMOND_ORES) ||
                block_state.is(BlockTags.EMERALD_ORES) ||
                block_state.is(BlockTags.LAPIS_ORES);
    }

    public static void check_farming(ServerPlayer player, BlockState block_state) {
        if (is_farming_block(block_state)) {
            if (trigger_farming(player, false)) {
                List<ItemStack> drops = Block.getDrops(block_state, (ServerLevel)player.level(), player.blockPosition(), null,
                                            player, player.getItemInHand(player.getUsedItemHand()));
                for (ItemStack drop : drops) {
                    drop.setCount(1);
                    Vec3 view_vec = player.getViewVector(0.5f);
                    ItemEntity item_entity = new ItemEntity(player.level(),
                            player.getX() + view_vec.x * 2,
                            player.getEyeY() + view_vec.y * 2,
                            player.getZ() + view_vec.z * 2, drop);
                    player.level().addFreshEntity(item_entity);
                }
            }
        }
    }


    public static boolean trigger_farming(ServerPlayer player, boolean animal_breeding) {
        int knowledge = player.getData(ModAttachments.FARMING_KNOWLEDGE);
        if (knowledge > 0 && player.getData(ModAttachments.FARMING_IS_ACTIVE)) {
            int spell_level = SpellUtil.get_farming_level(player);
            int cost = Util.get_or_last(Config.farming_cost, spell_level);
            float prob = Util.get_or_last(Config.farming_probability, spell_level);
            if (knowledge == 2 && animal_breeding ||
                knowledge == 3 && !animal_breeding) {
                //TODO
                prob *= 2f;
            }
            double player_mana = player.getData(PLAYER_MANA);
            if ((Config.test_mode || player_mana >= cost) && new Random().nextDouble() < prob) {
                Util.update_player_mana(player, player_mana - cost);
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.BEEHIVE_EXIT, SoundSource.PLAYERS, 2f, 0.5f);
//                player.level().addParticle(ParticleTypes.ELECTRIC_SPARK, player.getX(), player.getY(), player.getZ(), 0.0, 0.0, 0.0);
                if (animal_breeding) {
                    SpellUtil.increase_animals_bred(player, 1);
                }
                return true;
            }
        }

        return false;
    }


    private static boolean is_farming_block(BlockState block_state) {
        // early out
        if (block_state.is(Blocks.STONE) ||
            block_state.is(Blocks.DIRT) ||
            block_state.is(Blocks.GRASS_BLOCK)) {
            return false;
        }

        if (block_state.is(Blocks.MELON) ||
            block_state.is(Blocks.PUMPKIN) ||
            block_state.is(Blocks.CACTUS)
        ) {
            return true;
        }

        else if (block_state.is(Blocks.WHEAT) ||
                 block_state.is(Blocks.CARROTS) ||
                 block_state.is(Blocks.POTATOES) ||
                 block_state.is(Blocks.BEETROOTS)) {
            if (block_state.getValue(CropBlock.AGE) == 7) {
                return true;
            }
        }

        return false;
    }


    public static void clear_summoned_entities(ServerPlayer player, ServerLevel level) {
        Vec3 pos = player.getEyePosition();
        AABB box = new AABB(pos.x - 500, pos.y - 500, pos.z - 500, pos.x + 500, pos.y + 500, pos.z + 500);
        for (SummonedWitherSkeleton ent : level.getEntitiesOfClass(SummonedWitherSkeleton.class, box)) {
            if (ent.get_owner().equals(player)) {
                ent.discard();
            }
        }
        for (SummonedSkeleton ent : level.getEntitiesOfClass(SummonedSkeleton.class, box)) {
            if (ent.get_owner().equals(player)) {
                ent.discard();
            }
        }
        for (SummonedZombie ent : level.getEntitiesOfClass(SummonedZombie.class, box)) {
            if (ent.get_owner().equals(player)) {
                ent.discard();
            }
        }
        for (SummonedWarden ent : level.getEntitiesOfClass(SummonedWarden.class, box)) {
            if (ent.get_owner().equals(player)) {
                ent.discard();
            }
        }
        for (SummonedHorse ent : level.getEntitiesOfClass(SummonedHorse.class, box)) {
            if (ent.get_owner().equals(player)) {
                ent.discard();
            }
        }
    }


    public static int get_absorption_level(Player player) {
        return level_from_progression(Config.absorption_progression, player.getData(ABSORPTION_MITIGATION));
    }

    public static int get_repel_level(Player player) {
        return level_from_progression(Config.repel_progression, player.getData(REPEL_SPIDERS_REPELLED));
    }

    public static int get_fireball_level(Player player) {
        return level_from_progression(Config.fireball_progression, player.getData(FIREBALL_HITS));
    }

    public static int get_ice_cone_level(Player player) {
        return level_from_progression(Config.ice_cone_progression, player.getData(ICE_CONE_MULTIPLE_HIT));
    }

    public static int get_aether_grip_level(Player player) {
        return level_from_progression(Config.aether_grip_progression, player.getData(AETHER_GRIP_PULLS));
    }

    public static int get_haste_level(Player player) {
        return level_from_progression(Config.haste_progression, player.getData(HASTE_UPTIME).intValue() / 20);
    }

    public static int get_farming_level(Player player) {
        return level_from_progression(Config.farming_progression, player.getData(FARMING_ANIMALS_BRED));
    }

    public static int get_summon_ally_level(Player player) {
        return level_from_progression(Config.summon_ally_progression, player.getData(SUMMON_ALLY_SUMMONS));
    }

    public static void spell_fail_sound(Player player) {
        player.level().playSound(null, player.blockPosition(),
                    SoundEvents.DYE_USE, SoundSource.PLAYERS,
                    0.5f, 2f);
    }

    public static void spell_fail_indicators(ServerPlayer server_player, String spell_name, boolean enough_mana, boolean no_cd) {
        if (!enough_mana) {
            PacketDistributor.sendToPlayer(server_player, new NoManaPayload(false));
        }
        if (!no_cd) {
            PacketDistributor.sendToPlayer(server_player, new ActiveSpellCDPayload(spell_name));
        }
    }
}
