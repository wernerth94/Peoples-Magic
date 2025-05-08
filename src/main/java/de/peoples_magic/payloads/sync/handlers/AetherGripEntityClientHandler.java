package de.peoples_magic.payloads.sync.handlers;

import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.AetherGripEntityPayload;
import de.peoples_magic.payloads.sync.AetherGripPullsPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class AetherGripEntityClientHandler {

    public static void handleDataOnMain(final AetherGripEntityPayload data, final IPayloadContext context) {
        if (context.player().level().isClientSide) {
            context.player().setData(ModAttachments.AETHER_GRIP_ACTIVE_ENTITY, data.id());
        }
    }
}
