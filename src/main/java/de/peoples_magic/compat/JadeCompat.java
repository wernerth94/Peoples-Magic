package de.peoples_magic.compat;


import de.peoples_magic.block.ManaPoolBlock;
import de.peoples_magic.block.ModBlocks;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadeCompat implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(ManaPoolJadeCompat.INSTANCE, ModBlocks.MANA_POOL.get().getClass());
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(ManaPoolJadeCompat.INSTANCE, ModBlocks.MANA_POOL.get().getClass());
        //TODO register component providers, icon providers, callbacks, and config options here
    }
}
