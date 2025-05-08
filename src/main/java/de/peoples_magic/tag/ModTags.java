package de.peoples_magic.tag;

import de.peoples_magic.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModTags {

    public static class Enchantments {

        public static final TagKey<Enchantment> MANA_ENCHANTMENT = TagKey.create(Registries.ENCHANTMENT, Util.rec_loc("mana_enchantment"));
    }
}
