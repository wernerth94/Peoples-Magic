package de.peoples_magic.payloads.sync.handlers;

import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.AbsorptionMitigationPayload;
import de.peoples_magic.payloads.sync.AetherGripPullsPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class AetherGripPullsClientHandler {

    public static void handleDataOnMain(final AetherGripPullsPayload data, final IPayloadContext context) {
        if (context.player().level().isClientSide) {
            context.player().setData(ModAttachments.AETHER_GRIP_PULLS, data.pulls());
        }
    }
}
