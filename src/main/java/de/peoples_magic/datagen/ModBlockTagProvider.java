package de.peoples_magic.datagen;

import de.peoples_magic.block.ModBlocks;
import de.peoples_magic.PeoplesMagicMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, PeoplesMagicMod.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
//        tag(BlockTags.MINEABLE_WITH_PICKAXE)
//                .add(ModBlocks.BISMUTH_BLOCK.get())
//                .add(ModBlocks.BISMUTH_ORE.get())
//                .add(ModBlocks.BISMUTH_DEEPSLATE_ORE.get());
//
//        tag(BlockTags.NEEDS_IRON_TOOL)
//                .add(ModBlocks.BISMUTH_DEEPSLATE_ORE.get());

    }
}
