package de.peoples_magic.payloads.sync.handlers;

import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.overlays.FadingMessageOverlay;
import de.peoples_magic.payloads.sync.AbsorptionMitigationPayload;
import de.peoples_magic.payloads.sync.DisplayMessagePayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class DisplayMessageClientHandler {

    public static void handleDataOnMain(final DisplayMessagePayload data, final IPayloadContext context) {
        if (context.player().level().isClientSide) {
            FadingMessageOverlay.instance.show_message(data.msg(), data.time());
        }
    }
}
