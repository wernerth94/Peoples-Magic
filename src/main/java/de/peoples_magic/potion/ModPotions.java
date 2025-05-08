package de.peoples_magic.potion;

import de.peoples_magic.PeoplesMagicMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModPotions {

    public static final DeferredRegister<Potion> MOD_POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, PeoplesMagicMod.MOD_ID);

    public static final Holder<Potion> MANA_POTION = MOD_POTIONS.register("mana_potion",
            ManaPotion::new);


    public static void register(IEventBus bus) {
        MOD_POTIONS.register(bus);
    }
}
