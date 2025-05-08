package de.peoples_magic.block;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ModBlockStateProperties {
    public static final IntegerProperty MANA_WELL_STORED_MANA = IntegerProperty.create("mana_well_stored_mana", 0, 100);
    public static final IntegerProperty MANA_WELL_FILL_LEVEL = IntegerProperty.create("mana_well_fill_level", 0, 5);


    public static void load() {

    }
}
