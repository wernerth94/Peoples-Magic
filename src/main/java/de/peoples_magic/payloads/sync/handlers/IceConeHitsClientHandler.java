package de.peoples_magic.payloads.sync.handlers;

import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.AetherGripPullsPayload;
import de.peoples_magic.payloads.sync.IceConeHitsPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class IceConeHitsClientHandler {

    public static void handleDataOnMain(final IceConeHitsPayload data, final IPayloadContext context) {
        if (context.player().level().isClientSide) {
            context.player().setData(ModAttachments.ICE_CONE_MULTIPLE_HIT, data.multiple_hit());
        }
    }
}
