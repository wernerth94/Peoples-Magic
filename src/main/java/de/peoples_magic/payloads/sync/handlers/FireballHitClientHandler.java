package de.peoples_magic.payloads.sync.handlers;

import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.FireballHitPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class FireballHitClientHandler {

    public static void handleDataOnMain(final FireballHitPayload data, final IPayloadContext context) {
        if (context.player().level().isClientSide) {
            context.player().setData(ModAttachments.FIREBALL_HITS, data.amount());
        }
    }
}
