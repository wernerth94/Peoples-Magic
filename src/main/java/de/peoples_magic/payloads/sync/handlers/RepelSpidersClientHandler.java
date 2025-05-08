package de.peoples_magic.payloads.sync.handlers;

import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.FireballHitPayload;
import de.peoples_magic.payloads.sync.RepelSpidersPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class RepelSpidersClientHandler {

    public static void handleDataOnMain(final RepelSpidersPayload data, final IPayloadContext context) {
        if (context.player().level().isClientSide) {
            context.player().setData(ModAttachments.REPEL_SPIDERS_REPELLED.get(), data.amount());
        }
    }
}
