package de.peoples_magic;

import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.attributes.ModAttributes;
import de.peoples_magic.payloads.sync.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import static de.peoples_magic.attachments.ModAttachments.*;

public class Util {

    public static Vec3 angles_to_direction(float x, float y, float z) {
        float f = -Mth.sin(y * ((float)Math.PI / 180F)) * Mth.cos(x * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((x + z) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(y * ((float)Math.PI / 180F)) * Mth.cos(x * ((float)Math.PI / 180F));
        return new Vec3(f, f1, f2);
    }

    public static void tick_down_cooldowns(Player player, float amount) {
        for (AttachmentType<Float> spell : all_spell_cooldowns()) {
            if (player.getData(spell) > 0) {
                set_cooldown(player, spell, Math.max(0f, player.getData(spell) - amount));
            }
        }
    }

    public static void set_cooldown(Player player, AttachmentType<Float> spell, float seconds) {
        player.setData(spell, seconds);
        if (ABSORPTION_ACTIVE_CD.get() == spell) {
            PacketDistributor.sendToPlayer((ServerPlayer) player, new UpdateCooldownsPayload("absorption", seconds));
        }
        else if (REPEL_ACTIVE_CD.get() == spell) {
            PacketDistributor.sendToPlayer((ServerPlayer) player, new UpdateCooldownsPayload("repel", seconds));
        }
        else if (FIREBALL_ACTIVE_CD.get() == spell) {
            PacketDistributor.sendToPlayer((ServerPlayer) player, new UpdateCooldownsPayload("fireball", seconds));
        }
        else if (ICE_CONE_ACTIVE_CD.get() == spell) {
            PacketDistributor.sendToPlayer((ServerPlayer) player, new UpdateCooldownsPayload("ice_cone", seconds));
        }
        else if (AETHER_GRIP_ACTIVE_CD.get() == spell) {
            PacketDistributor.sendToPlayer((ServerPlayer) player, new UpdateCooldownsPayload("aether_grip", seconds));
        }
        else if (HASTE_ACTIVE_CD.get() == spell) {
            PacketDistributor.sendToPlayer((ServerPlayer) player, new UpdateCooldownsPayload("haste", seconds));
        }
        else if (FARMING_ACTIVE_CD.get() == spell) {
            PacketDistributor.sendToPlayer((ServerPlayer) player, new UpdateCooldownsPayload("farming", seconds));
        }
        else if (SUMMON_ALLY_ACTIVE_CD.get() == spell) {
            PacketDistributor.sendToPlayer((ServerPlayer) player, new UpdateCooldownsPayload("summon_ally", seconds));
        }
        else {
            throw new RuntimeException(String.format("Spell not recognized %s", spell.getClass()));
        }
    }

    public static void sync_player_mana(Player player) {
        PacketDistributor.sendToPlayer((ServerPlayer) player, new UpdateManaPayload(player.getData(PLAYER_MANA)));
    }

    public static void update_player_mana(Player player, double new_amount) {
        player.setData(PLAYER_MANA.get(), Math.max(0f, Math.min(player.getAttributeValue(ModAttributes.MAX_MANA), new_amount)));
        Util.sync_player_mana(player);
    }

    public static void update_spell_knowledge(ServerPlayer player, String name, int amount) {
        if (ModAttachments.all_spell_knowledge().containsKey(name)) {
            AttachmentType<Integer> spell = ModAttachments.all_spell_knowledge().get(name);
            player.setData(spell, amount);
            PacketDistributor.sendToPlayer(player, new UpdateKnowledgePayload(name, amount));
        }
        else {
            PeoplesMagicMod.LOGGER.error("[sync_spell_knowledge] Spell name {} not recognized", name);
        }
    }

    public static void sync_all_spell_knowledge(ServerPlayer player) {
        ModAttachments.all_spell_knowledge().forEach((name, spell) -> {
            PacketDistributor.sendToPlayer(player, new UpdateKnowledgePayload(name, player.getData(spell)));
        });
    }

    public static void sync_all_spell_data(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, new AbsorptionMitigationPayload(player.getData(ABSORPTION_MITIGATION)));
        PacketDistributor.sendToPlayer(player, new RepelSpidersPayload(player.getData(REPEL_SPIDERS_REPELLED)));
        PacketDistributor.sendToPlayer(player, new FireballHitPayload(player.getData(FIREBALL_HITS)));
        PacketDistributor.sendToPlayer(player, new AetherGripPullsPayload(player.getData(AETHER_GRIP_PULLS)));
        PacketDistributor.sendToPlayer(player, new IceConeHitsPayload((player.getData(ICE_CONE_MULTIPLE_HIT))));
        PacketDistributor.sendToPlayer(player, new HasteUptimePayload((player.getData(HASTE_UPTIME))));
        PacketDistributor.sendToPlayer(player, new HasteIsActivePayload((player.getData(HASTE_IS_ACTIVE))));
        PacketDistributor.sendToPlayer(player, new FarmingAnimalsBredPayload((player.getData(FARMING_ANIMALS_BRED))));
        PacketDistributor.sendToPlayer(player, new FarmingIsActivePayload((player.getData(FARMING_IS_ACTIVE))));
        PacketDistributor.sendToPlayer(player, new SummonAllySummonsPayload((player.getData(SUMMON_ALLY_SUMMONS))));
    }

    public static void sync_all_player_data(ServerPlayer server_player) {
        sync_player_mana(server_player);
        sync_all_spell_knowledge(server_player);
        sync_all_spell_data(server_player);
    }

    public static <T> T get_or_last(List<T> lst, int index) {
        if (index < 0) {
            return lst.get(0);
        }
        if (index < lst.size()) {
            return lst.get(index);
        }
        return lst.getLast();
    }

    public static <T extends Number> int level_from_progression(List<T> prog, Number value) {
        int level = 0;
        for (Number i : prog) {
            if (value.doubleValue() >= i.doubleValue()) {
                level++;
            }
            else {
                break;
            }
        }
        return level;
    }

    public static <T> ResourceKey<T> rec_key(ResourceKey<Registry<T>> registry, String id) {
        return ResourceKey.create(registry, Util.rec_loc(id));
    }


    public static ResourceLocation rec_loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(PeoplesMagicMod.MOD_ID, path);
    }


    public static boolean do_boxes_intersect(AABB box1, AABB box2) {
        return (box1.minX <= box2.maxX && box1.maxX >= box2.minX &&
                box1.minY <= box2.maxY && box1.maxY >= box2.minY &&
                box1.minZ <= box2.maxZ && box1.maxZ >= box2.minZ);
    }


    public static void draw_box(GuiGraphics guiGraphics, int x, int y, int max_x, int max_y, int color) {
        guiGraphics.fill(x, y, max_x, y+1, color);

        guiGraphics.fill(x, max_y-1, max_x,       max_y,   color);

        guiGraphics.fill(x, y, x+1, max_y, color);

        guiGraphics.fill(max_x-1, y, max_x, max_y, color);
    }
}
