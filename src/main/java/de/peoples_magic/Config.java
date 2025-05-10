package de.peoples_magic;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = PeoplesMagicMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue TEST_MODE = BUILDER.
            defineInRange("test_mode", 0, 0, 1);

//    private static final ModConfigSpec.DoubleValue MAX_MANA = BUILDER
//            .comment("Maximum amount of mana players can hold. Negative mana is not allowed")
//            .defineInRange("max_mana", 100F, 0, Double.MAX_VALUE);
//    private static final ModConfigSpec.DoubleValue BASE_MANA_REGENERATION = BUILDER
//            .comment("Amount of mana players naturally regenerates per second")
//            .defineInRange("base_mana_regeneration", 0.25F, 0, Double.MAX_VALUE);  // 0.25 mana / s
    private static final ModConfigSpec.DoubleValue MANA_WELL_MANA_PER_SECOND = BUILDER
            .comment("Amount of mana mana pools gain per second")
            .defineInRange("mana_well_mana_per_second", 0.33f, 0.2f, 20f);
    private static final ModConfigSpec.IntValue MANA_POTION_MANA_AMOUNT = BUILDER
            .comment("Amount of mana mana potions grants players")
            .defineInRange("mana_potion_mana_amount", 50, 0, Integer.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue MAX_MANA_PER_ENCHANT_LEVEL = BUILDER
            .comment("Amount of max mana increase per level of the max mana enchantment")
            .defineInRange("max_mana_per_enchant_level", 10F, 0, Double.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue MANA_REGEN_PER_ENCHANT_LEVEL = BUILDER
            .comment("Amount of mana regeneration increase per level of the mana regen enchantment")
            .defineInRange("mana_regen_per_enchant_level", 0.15F, 0, Double.MAX_VALUE);


    private static final ModConfigSpec.ConfigValue<List<Integer>> ICE_CONE_COST = BUILDER
            .comment("Amount of mana for the ice cone spell costs")
            .define("ice_cone_cost", List.of(10, 10, 12, 15, 15, 20, 25));
    private static final ModConfigSpec.ConfigValue<List<Float>> ICE_CONE_CDS = BUILDER
            .comment("Cooldown of the ice cone spell in seconds")
            .define("ice_cone_cd", List.of(10f, 9.5f, 9f, 8f, 7f, 6f, 5f));
    private static final ModConfigSpec.ConfigValue<List<Integer>> ICE_CONE_PROGRESSION = BUILDER
            .comment("Amount of multi-hits needed to progress the levels")
            .define("ice_cone_progression", List.of(10, 20, 40, 70, 110, 160, 220));
    private static final ModConfigSpec.ConfigValue<List<Integer>> ICE_CONE_AMPLIFIERS = BUILDER
            .comment("Amplifier of the slow effect")
            .define("ice_cone_amplifier", List.of(0, 0, 0, 1, 1, 1, 2));
    private static final ModConfigSpec.ConfigValue<List<Float>> ICE_CONE_DURATIONS = BUILDER
            .comment("Duration (in seconds) of the slow effect")
            .define("ice_cone_duration", List.of(3f, 3.5f, 4f, 4.5f, 5f, 5.5f, 6f));
    private static final ModConfigSpec.ConfigValue<List<Float>> ICE_CONE_DAMAGES = BUILDER
            .comment("Damage of the ice cone spell")
            .define("ice_cone_damage", List.of(2f, 2f, 2f, 3f, 3f, 3f, 4f));

    private static final ModConfigSpec.ConfigValue<List<Integer>> ABSORPTION_COST = BUILDER
            .comment("Amount of mana a the absorption spell costs")
            .define("absorption_cost", List.of(10, 10, 12, 15, 15, 20, 25));
    private static final ModConfigSpec.ConfigValue<List<Float>> ABSORPTION_CDS = BUILDER
            .comment("Cooldown of the absorption spell in seconds")
            .define("absorption_cd", List.of(10f, 9.5f, 9f, 8f, 7f, 6f, 5f));
    private static final ModConfigSpec.ConfigValue<List<Float>> ABSORPTION_PROGRESSION = BUILDER
            .comment("Amount of mitigated damage needed to progress the levels")
            .define("absorption_progression", List.of(10f, 20f, 40f, 100f, 200f, 500f, 1000f));
    private static final ModConfigSpec.ConfigValue<List<Integer>> ABSORPTION_AMPLIFIERS = BUILDER
            .comment("Amplifier of the absorption effect")
            .define("absorption_amplifier", List.of(0, 0, 0, 1, 1, 1, 2));
    private static final ModConfigSpec.ConfigValue<List<Float>> ABSORPTION_DURATIONS = BUILDER
            .comment("Duration (in seconds) of the absorption effect")
            .define("absorption_duration", List.of(1.5f, 1.8f, 2.1f, 2.4f, 2.7f, 3f, 3.3f));

    private static final ModConfigSpec.ConfigValue<List<Integer>> REPEL_COST = BUILDER
            .comment("Amount of mana a the repel spell costs")
            .define("repel_cost", List.of(15, 18, 21, 24, 27, 30, 40));
    private static final ModConfigSpec.ConfigValue<List<Float>> REPEL_CDS = BUILDER
            .comment("Cooldown of the repel spell in seconds")
            .define("repel_cd", List.of(10f, 9.5f, 9f, 8f, 7f, 6f, 5f));
    private static final ModConfigSpec.ConfigValue<List<Integer>> REPEL_PROGRESSION = BUILDER
            .comment("Amount of spiders repelled mid-air")
            .define("repel_progression", List.of(4, 8, 16, 30, 60, 100, 150));
    private static final ModConfigSpec.ConfigValue<List<Integer>> REPEL_RANGE = BUILDER
            .comment("Range of the repel effect")
            .define("repel_range", List.of(2, 2, 3, 3, 3, 4, 5));
    private static final ModConfigSpec.ConfigValue<List<Float>> REPEL_DURATIONS = BUILDER
            .comment("Duration (in seconds) of the repel effect")
            .define("repel_duration", List.of(2.5f, 3f, 3.5f, 4f, 4.5f, 5f, 6f));

    private static final ModConfigSpec.ConfigValue<List<Integer>> FIREBALL_COST = BUILDER
            .comment("Amount of mana of the fireball spell costs")
            .define("fireball_cost", List.of(10, 10, 12, 15, 15, 20, 25));
    private static final ModConfigSpec.ConfigValue<List<Float>> FIREBALL_CDS = BUILDER
            .comment("Cooldown of the fireball spell in seconds")
            .define("fireball_cd", List.of(10f, 9.5f, 9f, 8f, 7f, 6f, 5f));
    private static final ModConfigSpec.ConfigValue<List<Integer>> FIREBALL_PROGRESSION = BUILDER
            .comment("Amount of direct hits needed to progress the levels")
            .define("fireball_progression", List.of(20, 40, 80, 150, 230, 300, 400));
    private static final ModConfigSpec.ConfigValue<List<Float>> FIREBALL_KNOCKBACKS = BUILDER
            .comment("Knockback multiplier of the fireball projectile")
            .define("fireball_knockback", List.of(0.22f, 0.22f, 0.45f, 0.7f, 1.0f, 1.4f, 1.8f));
    private static final ModConfigSpec.ConfigValue<List<Float>> FIREBALL_VELOCITIES = BUILDER
            .comment("Speed of the fireball projectile")
            .define("fireball_velocity", List.of(1f, 1f, 1.2f, 1.5f, 1.7f, 2.0f, 2.3f));
    private static final ModConfigSpec.ConfigValue<List<Float>> FIREBALL_DAMAGES = BUILDER
            .comment("Bonus damage of direct hits with the fireball spell")
            .define("fireball_direct_damage", List.of(1f, 2f, 2.5f, 3f, 3.5f, 4f, 5f));

    private static final ModConfigSpec.ConfigValue<List<Integer>> AETHER_GRIP_COST = BUILDER
            .comment("Amount of mana of the aether grip spell costs")
            .define("aether_grip_cost", List.of(10,10,12,15,15,20,25));
    private static final ModConfigSpec.ConfigValue<List<Float>> AETHER_GRIP_CDS = BUILDER
            .comment("Cooldown of the aether grip spell in seconds")
            .define("aether_grip_cd", List.of(10f, 9.5f, 9f, 8f, 7f, 6f, 5f));
    private static final ModConfigSpec.ConfigValue<List<Integer>> AETHER_GRIP_PROGRESSION = BUILDER
            .comment("Amount of pulled skeletons needed to progress the levels")
            .define("aether_grip_progression", List.of(10, 20, 40, 70, 110, 160, 220));
    private static final ModConfigSpec.ConfigValue<List<Float>> AETHER_GRIP_DAMAGES = BUILDER
            .comment("Damage of the aether grip spell")
            .define("aether_grip_damage", List.of(2f, 2f, 2.5f, 3f, 4f, 5f, 6f));
    private static final ModConfigSpec.ConfigValue<List<Float>> AETHER_GRIP_PULL_STRENGTH = BUILDER
            .comment("Pull strength multiplier of the aether grip spell")
            .define("aether_grip_pull_strength", List.of(0.5f, 0.5f, 0.6f, 0.6f, 0.65f, 0.7f, 0.75f));
    private static final ModConfigSpec.ConfigValue<List<Float>> AETHER_GRIP_SPEED = BUILDER
            .comment("Speed of the aether grip projectile")
            .define("aether_grip_speed", List.of(1f, 1.1f, 1.2f, 1.3f, 1.4f, 1.5f, 1.6f));

    private static final ModConfigSpec.ConfigValue<List<Float>> HASTE_COST_PER_SECOND = BUILDER
            .comment("Amount of mana the haste spell costs per second")
            .define("haste_cost_per_second", List.of(2f, 2f, 1.5f, 1.5f, 1.5f, 1f, 1f));
    private static final ModConfigSpec.ConfigValue<List<Float>> HASTE_CDS = BUILDER
            .comment("Cooldown of the haste spell in seconds")
            .define("haste_cd", List.of(10f, 9.5f, 9f, 8f, 7f, 6f, 5f));
    private static final ModConfigSpec.ConfigValue<List<Integer>> HASTE_PROGRESSION = BUILDER
            .comment("Amount of uptime (in seconds) needed to progress the levels")
            .define("haste_progression", List.of(60, 180, 360, 720, 1200, 2000, 3000));
    private static final ModConfigSpec.ConfigValue<List<Integer>> HASTE_AMPLIFIERS = BUILDER
            .comment("Amplifier of the haste effect")
            .define("haste_amplifier", List.of(0, 0, 0, 1, 1, 1, 2));


    private static final ModConfigSpec.ConfigValue<List<Integer>> FARMING_COST = BUILDER
            .comment("Mana cost for duplicating an animal")
            .define("farming_cost", List.of(10, 9, 8, 7, 6, 5, 4));
    private static final ModConfigSpec.ConfigValue<List<Float>> FARMING_CDS = BUILDER
            .comment("Cooldown of the farming spell in seconds")
            .define("farming_cd", List.of(10f, 9.5f, 9f, 8f, 7f, 6f, 5f));
    private static final ModConfigSpec.ConfigValue<List<Integer>> FARMING_PROGRESSION = BUILDER
            .comment("Amount of animals bred needed to progress the levels")
            .define("farming_progression", List.of(10, 20, 40, 80, 140, 210, 300));
    private static final ModConfigSpec.ConfigValue<List<Float>> FARMING_PROBABILITY = BUILDER
            .comment("Probability of duplicating a bred animal")
            .define("farming_probability", List.of(0.2f, 0.25f, 0.25f, 0.3f, 0.3f, 0.4f, 0.45f));


    private static final ModConfigSpec.ConfigValue<List<Integer>> SUMMON_ALLY_COST = BUILDER
            .comment("Mana cost for casting summon ally")
            .define("summon_ally_cost", List.of(50, 50, 45, 45, 45, 40, 40));
    private static final ModConfigSpec.ConfigValue<List<Float>> SUMMON_ALLY_CDS = BUILDER
            .comment("Cooldown of the summon ally spell in seconds")
            .define("summon_ally_cd", List.of(30f, 27f, 24f, 21f, 18f, 15f, 12f));
    private static final ModConfigSpec.ConfigValue<List<Integer>> SUMMON_ALLY_PROGRESSION = BUILDER
            .comment("Amount of summoned allies needed to progress the levels")
            .define("summon_ally_progression", List.of(8, 16, 32, 64, 110, 200, 300));
    private static final ModConfigSpec.ConfigValue<List<Float>> SUMMON_ALLY_TIME_TO_LIFE = BUILDER
            .comment("Amount of time a summoned ally lives in seconds")
            .define("summon_ally_time_to_life", List.of(20f, 25f, 30f, 35f, 40f, 45f, 60f));


    private static final ModConfigSpec.IntValue BLAZEN_KNIGHT_HEALTH = BUILDER
            .comment("Amount of health in half-hearts for the blazen knight boss")
            .defineInRange("blazen_knight_health", 100, 1, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue BLAZEN_KNIGHT_ATTACK = BUILDER
            .comment("Amount of attack damage in half-hearts for the blazen knight boss")
            .defineInRange("blazen_knight_attack", 5, 0, Integer.MAX_VALUE);


    private static final ModConfigSpec.IntValue FOREST_GUARDIAN_HEALTH = BUILDER
            .comment("Amount of health in half-hearts for the forest guardian boss")
            .defineInRange("forest_guardian_health", 120, 1, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue FOREST_GUARDIAN_ATTACK = BUILDER
            .comment("Amount of attack damage in half-hearts for the forest guardian boss")
            .defineInRange("forest_guardian_attack", 15, 0, Integer.MAX_VALUE);


    private static final ModConfigSpec.IntValue SKY_SCOURGE_HEALTH = BUILDER
            .comment("Amount of health in half-hearts for the sky scourge boss")
            .defineInRange("sky_scourge_health", 200, 1, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue SKY_SCOURGE_ATTACK = BUILDER
            .comment("Amount of attack damage in half-hearts for the sky scourge boss")
            .defineInRange("sky_scourge_attack", 12, 0, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean test_mode;
    public static int blazen_knight_health;
    public static int blazen_knight_attack;
    public static int forest_guardian_health;
    public static int forest_guardian_attack;
    public static int sky_scourge_health;
    public static int sky_scourge_attack;


    public static double mana_well_mana_per_second;
    public static int mana_potion_mana_amount;
    public static double max_mana_per_enchant_level;
    public static double mana_regen_per_enchant_level;

    public static List<Integer> ice_cone_cost;
    public static List<Float> ice_cone_cds;
    public static List<Integer> ice_cone_progression;
    public static List<Float> ice_cone_damages;
    public static List<Float> ice_cone_durations;
    public static List<Integer> ice_cone_amplifiers;

    public static List<Integer> absorption_cost;
    public static List<Integer> absorption_amplifiers;
    public static List<Float> absorption_durations;
    public static List<Float> absorption_cds;
    public static List<Float> absorption_progression;

    public static List<Integer> repel_cost;
    public static List<Integer> repel_range;
    public static List<Float> repel_durations;
    public static List<Float> repel_cds;
    public static List<Integer> repel_progression;

    public static List<Integer> fireball_cost;
    public static List<Float> fireball_cds;
    public static List<Float> fireball_knockbacks;
    public static List<Float> fireball_velocities;
    public static List<Float> fireball_damages;
    public static List<Integer> fireball_progression;

    public static List<Integer> aether_grip_cost;
    public static List<Integer> aether_grip_progression;
    public static List<Float> aether_grip_cds;
    public static List<Float> aether_grip_damages;
    public static List<Float> aether_grip_pull_strength;
    public static List<Float> aether_grip_speed;

    public static List<Float> haste_cds;
    public static List<Float> haste_cost_per_s;
    public static List<Integer> haste_progression;
    public static List<Integer> haste_amplifiers;

    public static List<Float> farming_cds;
    public static List<Integer> farming_cost;
    public static List<Integer> farming_progression;
    public static List<Float> farming_probability;

    public static List<Float> summon_ally_cds;
    public static List<Integer> summon_ally_cost;
    public static List<Integer> summon_ally_progression;
    public static List<Float> summon_ally_time_to_live;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        test_mode = (TEST_MODE.get() == 1);
        blazen_knight_health = BLAZEN_KNIGHT_HEALTH.get();
        blazen_knight_attack = BLAZEN_KNIGHT_ATTACK.get();
        forest_guardian_health = FOREST_GUARDIAN_HEALTH.get();
        forest_guardian_attack = FOREST_GUARDIAN_ATTACK.get();
        sky_scourge_health = SKY_SCOURGE_HEALTH.get();
        sky_scourge_attack = SKY_SCOURGE_ATTACK.get();

        mana_well_mana_per_second = MANA_WELL_MANA_PER_SECOND.get();
        mana_potion_mana_amount = MANA_POTION_MANA_AMOUNT.get();
        max_mana_per_enchant_level = MAX_MANA_PER_ENCHANT_LEVEL.get();
        mana_regen_per_enchant_level = MANA_REGEN_PER_ENCHANT_LEVEL.get();

        ice_cone_cost = ICE_CONE_COST.get();
        ice_cone_cds = ICE_CONE_CDS.get();
        ice_cone_progression = ICE_CONE_PROGRESSION.get();
        ice_cone_damages = ICE_CONE_DAMAGES.get();
        ice_cone_amplifiers = ICE_CONE_AMPLIFIERS.get();
        ice_cone_durations = ICE_CONE_DURATIONS.get();

        absorption_cost = ABSORPTION_COST.get();
        absorption_amplifiers = ABSORPTION_AMPLIFIERS.get();
        absorption_durations = ABSORPTION_DURATIONS.get();
        absorption_cds = ABSORPTION_CDS.get();
        absorption_progression = ABSORPTION_PROGRESSION.get();

        repel_cost = REPEL_COST.get();
        repel_progression = REPEL_PROGRESSION.get();
        repel_cds = REPEL_CDS.get();
        repel_durations = REPEL_DURATIONS.get();
        repel_range = REPEL_RANGE.get();

        fireball_cost = FIREBALL_COST.get();
        fireball_cds = FIREBALL_CDS.get();
        fireball_knockbacks = FIREBALL_KNOCKBACKS.get();
        fireball_velocities = FIREBALL_VELOCITIES.get();
        fireball_damages = FIREBALL_DAMAGES.get();
        fireball_progression = FIREBALL_PROGRESSION.get();

        aether_grip_cost = AETHER_GRIP_COST.get();
        aether_grip_progression = AETHER_GRIP_PROGRESSION.get();
        aether_grip_cds = AETHER_GRIP_CDS.get();
        aether_grip_damages = AETHER_GRIP_DAMAGES.get();
        aether_grip_pull_strength = AETHER_GRIP_PULL_STRENGTH.get();
        aether_grip_speed = AETHER_GRIP_SPEED.get();

        haste_cds = HASTE_CDS.get();
        haste_cost_per_s = HASTE_COST_PER_SECOND.get();
        haste_progression = HASTE_PROGRESSION.get();
        haste_amplifiers = HASTE_AMPLIFIERS.get();

        farming_cds = FARMING_CDS.get();
        farming_cost = FARMING_COST.get();
        farming_progression = FARMING_PROGRESSION.get();
        farming_probability = FARMING_PROBABILITY.get();

        summon_ally_cds = SUMMON_ALLY_CDS.get();
        summon_ally_cost = SUMMON_ALLY_COST.get();
        summon_ally_progression = SUMMON_ALLY_PROGRESSION.get();
        summon_ally_time_to_live = SUMMON_ALLY_TIME_TO_LIFE.get();
    }
}
