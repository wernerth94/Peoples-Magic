package de.peoples_magic.datagen;

import de.peoples_magic.PeoplesMagicMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTagsProvider extends EnchantmentTagsProvider {
    public ModEnchantmentTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, PeoplesMagicMod.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Data gen didn't work for some reason, defined the tag manually
//        tag(ModTags.Enchantments.MANA_ENCHANTMENT)
//                .add(ModEnchantments.MANA_REGEN)
//                .add(ModEnchantments.MAX_MANA);
    }
}
