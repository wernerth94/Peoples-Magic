package de.peoples_magic.payloads.sync.handlers;

import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.HasteUptimePayload;
import de.peoples_magic.payloads.sync.UpdateManaPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class HasteUptimeClientHandler {

    public static void handleDataOnMain(final HasteUptimePayload data, final IPayloadContext context) {
        if (context.player().level().isClientSide) {
            context.player().setData(ModAttachments.HASTE_UPTIME, data.uptime());
        }
    }
}
