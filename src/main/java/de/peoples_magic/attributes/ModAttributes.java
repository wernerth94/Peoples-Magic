package de.peoples_magic.attributes;

import de.peoples_magic.PeoplesMagicMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, PeoplesMagicMod.MOD_ID);

    public static final DeferredHolder<Attribute, Attribute> MAX_MANA = ATTRIBUTES.register("max_mana",
            () -> new RangedAttribute("attribute.peoples_magic.max_mana", 100F, 0.0D, 1024.0D).setSyncable(true));

    public static final DeferredHolder<Attribute, Attribute> MANA_REGEN = ATTRIBUTES.register("mana_regen",
            () -> new RangedAttribute("attribute.peoples_magic.mana_regen", 0.25D, 0.0D, 1024.0D).setSyncable(true));

    public static void register(IEventBus modEventBus) {
        ATTRIBUTES.register(modEventBus);
    }
}
