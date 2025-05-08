package de.peoples_magic.attachments;

import com.mojang.serialization.Codec;
import de.peoples_magic.PeoplesMagicMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModAttachments {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, PeoplesMagicMod.MOD_ID);


    // Serialization via INBTSerializable
//    private static final Supplier<AttachmentType<FireballData>> HANDLER = ATTACHMENT_TYPES.register(
//            "handler", () -> AttachmentType.serializable(() -> new FireballData(1, 0.0f)).build()
//    );

    public static final Supplier<AttachmentType<Boolean>> BOM_RECEIVED = ATTACHMENT_TYPES.register(
            "player_bom_received", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).copyOnDeath().build()
    );

    public static final Supplier<AttachmentType<String>> BOM_ACTIVE_MENU = ATTACHMENT_TYPES.register(
            "bom_active_menu", () -> AttachmentType.builder(() -> "main").serialize(Codec.STRING).build()
    );

    public static final Supplier<AttachmentType<Double>> PLAYER_MANA = ATTACHMENT_TYPES.register(
            "player_mana", () -> AttachmentType.builder(() -> Double.valueOf(0)).serialize(Codec.DOUBLE).build()
    );
    public static final Supplier<AttachmentType<Boolean>> PLAYER_SYNCED = ATTACHMENT_TYPES.register(
            "player_synced_after_death", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build()
    );

    public static final Supplier<AttachmentType<Integer>> ABSORPTION_KNOWLEDGE = ATTACHMENT_TYPES.register(
            "absorption_knowledge", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());
    public static final Supplier<AttachmentType<Float>> ABSORPTION_ACTIVE_CD = ATTACHMENT_TYPES.register(
            "absorption_cd", () -> AttachmentType.builder(() -> 0.0f).serialize(Codec.FLOAT).copyOnDeath().build());
    public static final Supplier<AttachmentType<Long>> LAST_ABSORPTION_CAST = ATTACHMENT_TYPES.register(
            "last_absorption_cast", () -> AttachmentType.builder(() -> 0L).serialize(Codec.LONG).copyOnDeath().build());
    public static final Supplier<AttachmentType<Float>> ABSORPTION_MITIGATION = ATTACHMENT_TYPES.register(
            "absorption_mitigation", () -> AttachmentType.builder(() -> 0.0f).serialize(Codec.FLOAT).copyOnDeath().build());

    public static final Supplier<AttachmentType<Integer>> REPEL_KNOWLEDGE = ATTACHMENT_TYPES.register(
            "repel_knowledge", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());
    public static final Supplier<AttachmentType<Float>> REPEL_ACTIVE_CD = ATTACHMENT_TYPES.register(
            "repel_cd", () -> AttachmentType.builder(() -> 0.0f).serialize(Codec.FLOAT).copyOnDeath().build());
    public static final Supplier<AttachmentType<Long>> REPEL_CAST = ATTACHMENT_TYPES.register(
            "last_repel_cast", () -> AttachmentType.builder(() -> 0L).serialize(Codec.LONG).copyOnDeath().build());
    public static final Supplier<AttachmentType<Integer>> REPEL_SPIDERS_REPELLED = ATTACHMENT_TYPES.register(
            "repel_spiders_repelled", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());
    public static final Supplier<AttachmentType<Long>> REPEL_LAST_REPEL = ATTACHMENT_TYPES.register(
            "repel_last_repel", () -> AttachmentType.builder(() -> 0L).serialize(Codec.LONG).build());

    public static final Supplier<AttachmentType<Integer>> FIREBALL_KNOWLEDGE = ATTACHMENT_TYPES.register(
            "fireball_knowledge", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());
    public static final Supplier<AttachmentType<Float>> FIREBALL_ACTIVE_CD = ATTACHMENT_TYPES.register(
            "fireball_cd", () -> AttachmentType.builder(() -> 0.0f).serialize(Codec.FLOAT).copyOnDeath().build());
    public static final Supplier<AttachmentType<Integer>> FIREBALL_HITS = ATTACHMENT_TYPES.register(
            "fireball_hits", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());

    public static final Supplier<AttachmentType<Integer>> ICE_CONE_KNOWLEDGE = ATTACHMENT_TYPES.register(
            "ice_cone_knowledge", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());
    public static final Supplier<AttachmentType<Float>> ICE_CONE_ACTIVE_CD = ATTACHMENT_TYPES.register(
            "ice_cone_cd", () -> AttachmentType.builder(() -> 0.0f).serialize(Codec.FLOAT).copyOnDeath().build());
    public static final Supplier<AttachmentType<Integer>> ICE_CONE_MULTIPLE_HIT = ATTACHMENT_TYPES.register(
            "ice_cone_multiple_hit", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());

    public static final Supplier<AttachmentType<Integer>> AETHER_GRIP_KNOWLEDGE = ATTACHMENT_TYPES.register(
            "aether_grip_knowledge", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());
    public static final Supplier<AttachmentType<Float>> AETHER_GRIP_ACTIVE_CD = ATTACHMENT_TYPES.register(
            "aether_grip_cd", () -> AttachmentType.builder(() -> 0.0f).serialize(Codec.FLOAT).copyOnDeath().build());
    public static final Supplier<AttachmentType<Integer>> AETHER_GRIP_PULLS = ATTACHMENT_TYPES.register(
            "aether_grip_pulls", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());
    public static final Supplier<AttachmentType<Long>> AETHER_GRIP_PULL_CD = ATTACHMENT_TYPES.register(
            "aether_grip_pull_cd", () -> AttachmentType.builder(() -> 0L).serialize(Codec.LONG).copyOnDeath().build());
    public static final Supplier<AttachmentType<Integer>> AETHER_GRIP_ACTIVE_ENTITY = ATTACHMENT_TYPES.register(
            "aether_grip_active_entity", () -> AttachmentType.builder(() -> -1).serialize(Codec.INT).build());

    public static final Supplier<AttachmentType<Integer>> HASTE_KNOWLEDGE = ATTACHMENT_TYPES.register(
            "haste_knowledge", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());
    public static final Supplier<AttachmentType<Float>> HASTE_ACTIVE_CD = ATTACHMENT_TYPES.register(
            "haste_cd", () -> AttachmentType.builder(() -> 0.0f).serialize(Codec.FLOAT).copyOnDeath().build());
    public static final Supplier<AttachmentType<Float>> HASTE_UPTIME = ATTACHMENT_TYPES.register(
            "haste_uptime", () -> AttachmentType.builder(() -> 0f).serialize(Codec.FLOAT).build());
    public static final Supplier<AttachmentType<Boolean>> HASTE_IS_ACTIVE = ATTACHMENT_TYPES.register(
            "haste_is_active", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());
    public static final Supplier<AttachmentType<Integer>> HASTE_LAST_REFRESHED = ATTACHMENT_TYPES.register(
            "haste_last_refreshed", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());

    public static final Supplier<AttachmentType<Integer>> FARMING_KNOWLEDGE = ATTACHMENT_TYPES.register(
            "farming_knowledge", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());
    public static final Supplier<AttachmentType<Float>> FARMING_ACTIVE_CD = ATTACHMENT_TYPES.register(
            "farming_cd", () -> AttachmentType.builder(() -> 0.0f).serialize(Codec.FLOAT).copyOnDeath().build());
    public static final Supplier<AttachmentType<Integer>> FARMING_ANIMALS_BRED = ATTACHMENT_TYPES.register(
            "farming_animals_bred", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());
    public static final Supplier<AttachmentType<Boolean>> FARMING_IS_ACTIVE = ATTACHMENT_TYPES.register(
            "farming_is_active", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).copyOnDeath().build());

    public static final Supplier<AttachmentType<Integer>> SUMMON_ALLY_KNOWLEDGE = ATTACHMENT_TYPES.register(
            "summon_ally_knowledge", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());
    public static final Supplier<AttachmentType<Float>> SUMMON_ALLY_ACTIVE_CD = ATTACHMENT_TYPES.register(
            "summon_ally_cd", () -> AttachmentType.builder(() -> 0.0f).serialize(Codec.FLOAT).copyOnDeath().build());
    public static final Supplier<AttachmentType<Integer>> SUMMON_ALLY_SUMMONS = ATTACHMENT_TYPES.register(
            "summon_ally_summons", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build());


    public static Map<String, AttachmentType<Integer>> all_spell_knowledge() {
        return Map.of(
                "absorption", ABSORPTION_KNOWLEDGE.get(),
                "repel", REPEL_KNOWLEDGE.get(),
                "fireball", FIREBALL_KNOWLEDGE.get(),
                "ice_cone", ICE_CONE_KNOWLEDGE.get(),
                "aether_grip", AETHER_GRIP_KNOWLEDGE.get(),
                "haste", HASTE_KNOWLEDGE.get(),
                "farming", FARMING_KNOWLEDGE.get(),
                "summon_ally", SUMMON_ALLY_KNOWLEDGE.get()
        );
    }

    public static List<AttachmentType<Float>> all_spell_cooldowns() {
        return List.of(
                ABSORPTION_ACTIVE_CD.get(),
                REPEL_ACTIVE_CD.get(),
                FIREBALL_ACTIVE_CD.get(),
                ICE_CONE_ACTIVE_CD.get(),
                AETHER_GRIP_ACTIVE_CD.get(),
                HASTE_ACTIVE_CD.get(),
                FARMING_ACTIVE_CD.get(),
                SUMMON_ALLY_ACTIVE_CD.get()
        );
    }

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
