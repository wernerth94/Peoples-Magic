package de.peoples_magic.compat;

import de.peoples_magic.Util;
import de.peoples_magic.block.ModBlocks;
import snownee.jade.api.*;

@WailaPlugin
public class JadeCompat implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(new ManaPoolJadeCompat(), ModBlocks.MANA_POOL.get().getClass());
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new ManaPoolJadeClientCompat(), ModBlocks.MANA_POOL.get().getClass());
    }

}
