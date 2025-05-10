package de.peoples_magic.block;

import de.peoples_magic.PeoplesMagicMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.function.Supplier;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, PeoplesMagicMod.MOD_ID);

    public static final Supplier<BlockEntityType<ManaPoolBlockEntity>> MANA_POOL_BLOCK_ENTITY = BLOCK_ENTITIES.register("mana_pool_block_entity",
            () -> new BlockEntityType<>(ManaPoolBlockEntity::new, ModBlocks.MANA_POOL.get()));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
