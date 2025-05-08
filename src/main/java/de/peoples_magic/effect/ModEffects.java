package de.peoples_magic.effect;

import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, PeoplesMagicMod.MOD_ID);


    public static final Holder<MobEffect> MANA_POTION_EFFECT = MOB_EFFECTS.register("mana_potion",
            () -> new ManaPotionEffect(0x329af0));

    public static final Holder<MobEffect> REPEL_EFFECT = MOB_EFFECTS.register("repel",
            () -> new RepelEffect(0x161fbd));

    public static final Holder<MobEffect> THORNS_EFFECT = MOB_EFFECTS.register("thorns",
            () -> new ThornsEffect(0x161fbd));

    public static final Holder<MobEffect> STASIS_EFFECT = MOB_EFFECTS.register("stasis",
            () -> new StasisEffect(0x161fbd));

    public static final Holder<MobEffect> APPLY_STASIS_EFFECT = MOB_EFFECTS.register("apply_stasis",
            () -> new ApplyStasisEffect(0x161fbd));

    public static final Holder<MobEffect> LIGHTNING_STORM_EFFECT = MOB_EFFECTS.register("lightning_storm",
            () -> new LightningStormEffect(0x161fbd));

    public static final Holder<MobEffect> ABSOLUTE_ZERO_EFFECT = MOB_EFFECTS.register("absolute_zero",
            () -> new AbsoluteZeroEffect(0x161fbd));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
