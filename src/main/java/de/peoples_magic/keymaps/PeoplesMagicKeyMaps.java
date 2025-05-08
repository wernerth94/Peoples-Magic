package de.peoples_magic.keymaps;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

public class PeoplesMagicKeyMaps {

    public static final Lazy<KeyMapping> SPELL_ABSORPTION_MAPPING = Lazy.of(() -> new KeyMapping(
            "key.peoples_magic.spell_absorption_mapping",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "key.categories.peoples_magic"
    ));

    public static final Lazy<KeyMapping> SPELL_REPEL_MAPPING = Lazy.of(() -> new KeyMapping(
            "key.peoples_magic.spell_repel_mapping",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "key.categories.peoples_magic"
    ));

    public static final Lazy<KeyMapping> SPELL_FIREBALL_MAPPING = Lazy.of(() -> new KeyMapping(
            "key.peoples_magic.spell_fireball_mapping",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "key.categories.peoples_magic"
    ));

    public static final Lazy<KeyMapping> SPELL_ICE_CONE_MAPPING = Lazy.of(() -> new KeyMapping(
            "key.peoples_magic.spell_ice_cone_mapping",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "key.categories.peoples_magic"
    ));

    public static final Lazy<KeyMapping> SPELL_AETHER_GRIP_MAPPING = Lazy.of(() -> new KeyMapping(
            "key.peoples_magic.spell_aether_grip_mapping",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "key.categories.peoples_magic"
    ));

    public static final Lazy<KeyMapping> SPELL_HASTE_MAPPING = Lazy.of(() -> new KeyMapping(
            "key.peoples_magic.spell_haste_mapping",
            InputConstants.Type.MOUSE,
            GLFW.GLFW_MOUSE_BUTTON_4,
            "key.categories.peoples_magic"
    ));

    public static final Lazy<KeyMapping> SPELL_FARMING_MAPPING = Lazy.of(() -> new KeyMapping(
            "key.peoples_magic.spell_farming_mapping",
            InputConstants.Type.MOUSE,
            GLFW.GLFW_MOUSE_BUTTON_5,
            "key.categories.peoples_magic"
    ));

    public static final Lazy<KeyMapping> SPELL_SUMMON_ALLY_MAPPING = Lazy.of(() -> new KeyMapping(
            "key.peoples_magic.spell_summon_ally_mapping",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "key.categories.peoples_magic"
    ));



    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(SPELL_ABSORPTION_MAPPING.get());
        event.register(SPELL_REPEL_MAPPING.get());
        event.register(SPELL_FIREBALL_MAPPING.get());
        event.register(SPELL_ICE_CONE_MAPPING.get());
        event.register(SPELL_AETHER_GRIP_MAPPING.get());
        event.register(SPELL_HASTE_MAPPING.get());
        event.register(SPELL_FARMING_MAPPING.get());
        event.register(SPELL_SUMMON_ALLY_MAPPING.get());
    }
}
