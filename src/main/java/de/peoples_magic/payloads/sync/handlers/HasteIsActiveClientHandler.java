package de.peoples_magic.payloads.sync.handlers;

import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.AetherGripEntityPayload;
import de.peoples_magic.payloads.sync.HasteIsActivePayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class HasteIsActiveClientHandler {

    public static void handleDataOnMain(final HasteIsActivePayload data, final IPayloadContext context) {
        if (context.player().level().isClientSide) {
            context.player().setData(ModAttachments.HASTE_IS_ACTIVE, data.is_active());
        }
    }
}
