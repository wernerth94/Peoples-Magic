package de.peoples_magic.payloads.sync.handlers;

import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.FarmingIsActivePayload;
import de.peoples_magic.payloads.sync.HasteIsActivePayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class FarmingIsActiveClientHandler {

    public static void handleDataOnMain(final FarmingIsActivePayload data, final IPayloadContext context) {
        if (context.player().level().isClientSide) {
            context.player().setData(ModAttachments.FARMING_IS_ACTIVE, data.is_active());
        }
    }
}
