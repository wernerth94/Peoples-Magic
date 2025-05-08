package de.peoples_magic.sound;

import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, PeoplesMagicMod.MOD_ID);

    public static final Supplier<SoundEvent> ICE_CONE_CAST = register_sound("ice_cone_cast");
    public static final Supplier<SoundEvent> ICE_CONE_HIT = register_sound("ice_cone_hit");
    public static final Supplier<SoundEvent> SUMMON_ALLY = register_sound("summon_ally");


    private static Supplier<SoundEvent> register_sound(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(Util.rec_loc(name)));
    }

    public static void register(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }
}
