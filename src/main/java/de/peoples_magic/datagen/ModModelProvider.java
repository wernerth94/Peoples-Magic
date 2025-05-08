package de.peoples_magic.datagen;

import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.Util;
import de.peoples_magic.block.ManaPoolBlock;
import de.peoples_magic.block.ModBlocks;
import de.peoples_magic.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.data.PackOutput;

import java.util.HashSet;
import java.util.SequencedSet;
import java.util.Set;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput pOutput) {
        super(pOutput, PeoplesMagicMod.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        register_items(itemModels);
        register_blocks(blockModels);
    }


    private void register_blocks(BlockModelGenerators blockModels) {
        MultiVariant base = BlockModelGenerators.variant(new Variant(Util.rec_loc("block/mana_pool_0")));
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(ModBlocks.MANA_POOL.get())
                        .with(PropertyDispatch.initial(ManaPoolBlock.FILL_LEVEL)
                                .select(0, base.with(VariantMutator.MODEL.withValue(Util.rec_loc("block/mana_pool_0"))))
                                .select(1, base.with(VariantMutator.MODEL.withValue(Util.rec_loc("block/mana_pool_1"))))
                                .select(2, base.with(VariantMutator.MODEL.withValue(Util.rec_loc("block/mana_pool_2"))))
                                .select(3, base.with(VariantMutator.MODEL.withValue(Util.rec_loc("block/mana_pool_3"))))
                                .select(4, base.with(VariantMutator.MODEL.withValue(Util.rec_loc("block/mana_pool_4"))))
                                .select(5, base.with(VariantMutator.MODEL.withValue(Util.rec_loc("block/mana_pool_5")))))
        );
        // declare the item manually in register_items
    }


    private void register_items(ItemModelGenerators itemModels) {
        // This makes it possible to define assets/models/item/mana_pool.json to render a non-flat model
        itemModels.declareCustomModelItem(ModBlocks.MANA_POOL.asItem());

        itemModels.generateFlatItem(ModItems.BOOK_OF_MAGIC.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SUSPICIOUS_TOME.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.TOME_OF_ABSORPTION.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.TOME_OF_FIREBALL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.TOME_OF_ICE_CONE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.TOME_OF_AETHER_GRIP.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.TOME_OF_HASTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.TOME_OF_REPEL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.TOME_OF_FARMING.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.TOME_OF_SUMMON_ALLY.get(), ModelTemplates.FLAT_ITEM);
    }
}