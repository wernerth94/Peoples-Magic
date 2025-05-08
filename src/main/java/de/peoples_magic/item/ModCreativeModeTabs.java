package de.peoples_magic.item;

import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.block.ModBlocks;
import de.peoples_magic.potion.ModPotions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> MOD_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, PeoplesMagicMod.MOD_ID);


    public static final Supplier<CreativeModeTab> PEOPLES_MAGIC_TAB = MOD_TABS.register("peoples_magic_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.TOME_OF_FIREBALL.get()))
                    .title(Component.translatable("creativetab.peoples_magic.peoples_magic_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.BOOK_OF_MAGIC);
                        output.accept(ModItems.SUSPICIOUS_TOME);
                        output.accept(ModBlocks.MANA_POOL);
                        output.accept(ModItems.TOME_OF_ABSORPTION);
                        output.accept(ModItems.TOME_OF_FIREBALL);
                        output.accept(ModItems.TOME_OF_ICE_CONE);
                        output.accept(ModItems.TOME_OF_AETHER_GRIP);
                        output.accept(ModItems.TOME_OF_HASTE);
                        output.accept(ModItems.TOME_OF_FARMING);
                        output.accept(ModItems.TOME_OF_SUMMON_ALLY);
                        output.accept(ModItems.TOME_OF_REPEL);
                    })
                    .build());

    public static void register(IEventBus bus) {
        MOD_TABS.register(bus);
    }
}
