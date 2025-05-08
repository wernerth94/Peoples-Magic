package de.peoples_magic.payloads.sync.handlers;

import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.AbsorptionMitigationPayload;
import de.peoples_magic.payloads.sync.UpdateManaPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class AbsorptionMitigationClientHandler {

    public static void handleDataOnMain(final AbsorptionMitigationPayload data, final IPayloadContext context) {
        if (context.player().level().isClientSide) {
            context.player().setData(ModAttachments.ABSORPTION_MITIGATION, data.mitigation());
        }
    }
}
