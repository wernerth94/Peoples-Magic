package de.peoples_magic.datagen;

import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.item.ModItems;
import de.peoples_magic.loottables.AddItemModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {

    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, PeoplesMagicMod.MOD_ID);
    }

    @Override
    protected void start() {
        // Absorption
        add("abandoned_mineshaft",
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse("chests/abandoned_mineshaft")).build(),
                        LootItemRandomChanceCondition.randomChance(0.6f).build()
                }, ModItems.TOME_OF_ABSORPTION.get()) );

        // Fireball
        add("bastion_treasure",
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse("chests/bastion_treasure")).build(),
                        LootItemRandomChanceCondition.randomChance(0.6f).build()
                }, ModItems.TOME_OF_FIREBALL.get()) );
        add("bastion_other",
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse("chests/bastion_other")).build(),
                        LootItemRandomChanceCondition.randomChance(0.2f).build()
                }, ModItems.TOME_OF_FIREBALL.get()) );
        add("bastion_bridge",
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse("chests/bastion_bridge")).build(),
                        LootItemRandomChanceCondition.randomChance(0.2f).build()
                }, ModItems.TOME_OF_FIREBALL.get()) );
        add("nether_bridge",
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse("chests/nether_bridge")).build(),
                        LootItemRandomChanceCondition.randomChance(0.4f).build()
                }, ModItems.TOME_OF_FIREBALL.get()) );

        // Ice Cone
        add("shipwreck_treasure",
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse("chests/shipwreck_treasure")).build(),
                        LootItemRandomChanceCondition.randomChance(0.5f).build()
                }, ModItems.TOME_OF_ICE_CONE.get()) );
        add("shipwreck_map",
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse("chests/shipwreck_map")).build(),
                        LootItemRandomChanceCondition.randomChance(0.4f).build()
                }, ModItems.TOME_OF_ICE_CONE.get()) );

        // Aether Grip
        add("stronghold_library",
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse("chests/stronghold_library")).build(),
                        LootItemRandomChanceCondition.randomChance(0.3f).build()
                }, ModItems.TOME_OF_AETHER_GRIP.get()) );
        add("end_city_treasure",
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse("chests/end_city_treasure")).build(),
                        LootItemRandomChanceCondition.randomChance(0.6f).build()
                }, ModItems.TOME_OF_AETHER_GRIP.get()) );

        // Haste
        add("jungle_temple",
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse("chests/jungle_temple")).build(),
                        LootItemRandomChanceCondition.randomChance(1.0f).build()
                }, ModItems.TOME_OF_HASTE.get()) );
        add("desert_pyramid",
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse("chests/desert_pyramid")).build(),
                        LootItemRandomChanceCondition.randomChance(0.8f).build()
                }, ModItems.TOME_OF_HASTE.get()) );

        // Farming
        add("underwater_ruin_small",
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse("chests/underwater_ruin_small")).build(),
                        LootItemRandomChanceCondition.randomChance(0.4f).build()
                }, ModItems.TOME_OF_FARMING.get()) );
        add("underwater_ruin_big",
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse("chests/underwater_ruin_big")).build(),
                        LootItemRandomChanceCondition.randomChance(0.5f).build()
                }, ModItems.TOME_OF_FARMING.get()) );

        // Repel
        add("woodland_mansion",
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse("chests/woodland_mansion")).build(),
                        LootItemRandomChanceCondition.randomChance(0.7f).build()
                }, ModItems.TOME_OF_REPEL.get()) );
        add("pillager_outpost",
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse("chests/pillager_outpost")).build(),
                        LootItemRandomChanceCondition.randomChance(0.6f).build()
                }, ModItems.TOME_OF_REPEL.get()) );

        // Summon Ally
        add("ancient_city",
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse("chests/ancient_city")).build(),
                        LootItemRandomChanceCondition.randomChance(0.6f).build()
                }, ModItems.TOME_OF_SUMMON_ALLY.get()) );


        add_all_tomes("ancient_city", 0.1f);
        add_all_tomes("igloo_chest", 0.6f);
        add_all_tomes("buried_treasure", 0.2f);
        add_all_tomes("trial_chambers/reward", 0.2f);
    }

    private void add_all_tomes(String location, float chance) {
        add(String.format("%s_absorption", location),
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse(String.format("chests/%s", location))).build(),
                        LootItemRandomChanceCondition.randomChance(chance).build()
                }, ModItems.TOME_OF_ABSORPTION.get()) );
        add(String.format("%s_aether_grip", location),
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse(String.format("chests/%s", location))).build(),
                        LootItemRandomChanceCondition.randomChance(chance).build()
                }, ModItems.TOME_OF_AETHER_GRIP.get()) );
        add(String.format("%s_fireball", location),
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse(String.format("chests/%s", location))).build(),
                        LootItemRandomChanceCondition.randomChance(chance).build()
                }, ModItems.TOME_OF_FIREBALL.get()) );
        add(String.format("%s_ice_cone", location),
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse(String.format("chests/%s", location))).build(),
                        LootItemRandomChanceCondition.randomChance(chance).build()
                }, ModItems.TOME_OF_ICE_CONE.get()) );
        add(String.format("%s_repel", location),
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse(String.format("chests/%s", location))).build(),
                        LootItemRandomChanceCondition.randomChance(chance).build()
                }, ModItems.TOME_OF_REPEL.get()) );
        add(String.format("%s_farming", location),
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse(String.format("chests/%s", location))).build(),
                        LootItemRandomChanceCondition.randomChance(chance).build()
                }, ModItems.TOME_OF_FARMING.get()) );
        add(String.format("%s_haste", location),
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse(String.format("chests/%s", location))).build(),
                        LootItemRandomChanceCondition.randomChance(chance).build()
                }, ModItems.TOME_OF_HASTE.get()) );
        add(String.format("%s_summon_ally", location),
                new AddItemModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.parse(String.format("chests/%s", location))).build(),
                        LootItemRandomChanceCondition.randomChance(chance).build()
                }, ModItems.TOME_OF_SUMMON_ALLY.get()) );
    }
}
