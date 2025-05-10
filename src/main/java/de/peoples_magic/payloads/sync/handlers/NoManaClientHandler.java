package de.peoples_magic.payloads.sync.handlers;

import de.peoples_magic.overlays.ManaBarOverlay;
import de.peoples_magic.payloads.sync.DisplayMessagePayload;
import de.peoples_magic.payloads.sync.NoManaPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class NoManaClientHandler {

    public static void handleDataOnMain(final NoManaPayload data, final IPayloadContext context) {
        if (context.player().level().isClientSide) {
            ManaBarOverlay.instance.no_mana_flash();
        }
    }
}
