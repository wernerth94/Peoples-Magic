package de.peoples_magic.loottables;

import com.mojang.serialization.MapCodec;
import de.peoples_magic.PeoplesMagicMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModLootModifiers {
    public static DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, PeoplesMagicMod.MOD_ID);


    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<AddItemModifier>> ADD_ITEM_MODIFIER =
            LOOT_MODIFIERS.register("add_item_modifier", AddItemModifier.CODEC);


    public static void register(IEventBus bus) {
        LOOT_MODIFIERS.register(bus);
    }
}
