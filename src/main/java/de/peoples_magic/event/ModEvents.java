package de.peoples_magic.event;

import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.command.*;
import de.peoples_magic.enchantment.ModEnchantments;
import de.peoples_magic.item.ModItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.neoforged.neoforge.server.command.ConfigCommand;
import java.util.List;

@EventBusSubscriber(modid = PeoplesMagicMod.MOD_ID, bus=EventBusSubscriber.Bus.GAME)
public class ModEvents {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new SetFireballDmgCommand(event.getDispatcher());
        new SetAbsorptionMitigationCommand(event.getDispatcher());
        new SetAetherGripPullsCommand(event.getDispatcher());
        new SetIceConeMultipleHitsCommand(event.getDispatcher());
        new FireballProgressionCommand(event.getDispatcher());
        new AbsorptionProgressionCommand(event.getDispatcher());
        new AetherGripProgressionCommand(event.getDispatcher());
        new LearnAllSpellsCommand(event.getDispatcher());
        new UnlearnAllSpellsCommand(event.getDispatcher());
        new SummonAllyCommand(event.getDispatcher());
        new SetAllySummonsCommand(event.getDispatcher());
        new SummonBlazenKnightCommand(event.getDispatcher());
        new SummonSkyScourgeCommand(event.getDispatcher());
        new SummonForestGuardianCommand(event.getDispatcher());
        new SetRepelSpidersRepelledCommand(event.getDispatcher());
        new SetFarmingBreedsCommand(event.getDispatcher());
        new SetHasteUptimeCommand(event.getDispatcher());
        new ResetExpertisesCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        HolderLookup.RegistryLookup<Enchantment> registry = event.getRegistries().lookup(Registries.ENCHANTMENT).get();
        if (event.getType() == VillagerProfession.LIBRARIAN) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            add_all_enchants(registry, trades, 1, 5, 1);
            add_all_enchants(registry, trades, 2, 5, 5);
            add_all_enchants(registry, trades, 3, 5, 10);
            add_all_enchants(registry, trades, 5, 5, 30);
        }
    }


    private static void add_all_enchants(HolderLookup.RegistryLookup<Enchantment> registry, Int2ObjectMap<List<VillagerTrades.ItemListing>> trades,
                                         int level, int max_uses, int xp) {
        List<ResourceKey<Enchantment>> all_enchants = List.of(ModEnchantments.MANA_REGEN, ModEnchantments.MAX_MANA);

        for (ResourceKey<Enchantment> ench : all_enchants) {
            trades.get(level).add((pTrader, pRandom) -> new MerchantOffer(new ItemCost(Items.EMERALD, 10),
                    create_for_enchantment(registry.getOrThrow(ench), 1),
                    max_uses, xp, 0.1F)
            );
            trades.get(level).add((pTrader, pRandom) -> new MerchantOffer(new ItemCost(Items.EMERALD, 20),
                    create_for_enchantment(registry.getOrThrow(ench), 2),
                    max_uses, xp, 0.1F)
            );
            trades.get(level).add((pTrader, pRandom) -> new MerchantOffer(new ItemCost(Items.EMERALD, 30),
                    create_for_enchantment(registry.getOrThrow(ench), 3),
                    max_uses, xp, 0.1F)
            );
            if (ench == ModEnchantments.MANA_LEECH) {
                trades.get(level).add((pTrader, pRandom) -> new MerchantOffer(new ItemCost(Items.EMERALD, 40),
                        create_for_enchantment(registry.getOrThrow(ench), 4),
                        max_uses, xp, 0.1F)
                );
            }
        }
    }


    private static ItemStack create_for_enchantment(Holder.Reference<Enchantment> ench, int level) {
        ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);
        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantments.set(ench, level);
        result.set(DataComponents.STORED_ENCHANTMENTS, enchantments.toImmutable());
        return result;
    }


    @SubscribeEvent
    public static void addWanderingTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> generic_trades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rare_trades = event.getRareTrades();

        List<Item> all_tomes = List.of(
                ModItems.TOME_OF_FIREBALL.get(),
                ModItems.TOME_OF_ICE_CONE.get(),
                ModItems.TOME_OF_ABSORPTION.get(),
                ModItems.TOME_OF_AETHER_GRIP.get(),
                ModItems.TOME_OF_HASTE.get(),
                ModItems.TOME_OF_FARMING.get(),
                ModItems.TOME_OF_REPEL.get(),
                ModItems.TOME_OF_SUMMON_ALLY.get()
        );

        for (Item tome : all_tomes) {
            rare_trades.add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 64),
                    new ItemStack(tome, 1),
                    1, 10, 0.1F));

        }
    }
}
