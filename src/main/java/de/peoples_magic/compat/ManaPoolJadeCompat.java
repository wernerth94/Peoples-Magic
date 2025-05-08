package de.peoples_magic.compat;

import de.peoples_magic.Util;
import de.peoples_magic.block.ManaPoolBlock;
import de.peoples_magic.block.ModBlocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.impl.ui.TextElement;

public enum ManaPoolJadeCompat implements
        IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(
            ITooltip tooltip,
            BlockAccessor accessor,
            IPluginConfig config
    ) {
        if (accessor.getServerData().contains("Mana") &&
            accessor.getServerData().getInt("Mana").isPresent()) {

            tooltip.add(new TextElement(Component.translatable(
                    "description.peoples_magic.mana_pool_stored_mana",
                    accessor.getServerData().getInt("Mana").get()
            )));
        }
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        data.putInt("Mana", accessor.getBlockState().getValue(ManaPoolBlock.MANA_STORED));
    }

    @Override
    public ResourceLocation getUid() {
        return Util.rec_loc("jade_mana_pool");
    }

}
