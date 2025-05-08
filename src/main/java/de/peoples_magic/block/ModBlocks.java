package de.peoples_magic.block;

import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.Util;
import de.peoples_magic.item.ModItems;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(PeoplesMagicMod.MOD_ID);

    public static final DeferredBlock<Block> MANA_POOL = registerBlock("mana_pool",
            () -> new ManaPoolBlock(BlockBehaviour.Properties.of()
                    .setId(Util.rec_key(Registries.BLOCK, "mana_pool"))
                    .randomTicks().strength(4f).noOcclusion()));
    // Important to use the specific block class for complex blocks



    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().useBlockDescriptionPrefix().setId(Util.rec_key(Registries.ITEM, name))));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);

//        ItemBlockRenderTypes.setRenderLayer(MANA_POOL.get(), RenderType.translucent());
    }
}
