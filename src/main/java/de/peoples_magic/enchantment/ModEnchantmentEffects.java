package de.peoples_magic.enchantment;

import com.mojang.serialization.MapCodec;
import de.peoples_magic.PeoplesMagicMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEnchantmentEffects {
    public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> ENITITY_ENCHANTMENT_EFFECTS =
            DeferredRegister.create(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, PeoplesMagicMod.MOD_ID);

    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> MANA_LEECH =
            ENITITY_ENCHANTMENT_EFFECTS.register("mana_leech", () -> ManaLeechEnchantmentEffect.CODEC);


    public static void register(IEventBus eventBus) {
        ENITITY_ENCHANTMENT_EFFECTS.register(eventBus);
    }
}
