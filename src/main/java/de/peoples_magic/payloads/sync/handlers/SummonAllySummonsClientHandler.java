package de.peoples_magic.payloads.sync.handlers;

import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.IceConeHitsPayload;
import de.peoples_magic.payloads.sync.SummonAllySummonsPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SummonAllySummonsClientHandler {

    public static void handleDataOnMain(final SummonAllySummonsPayload data, final IPayloadContext context) {
        if (context.player().level().isClientSide) {
            context.player().setData(ModAttachments.SUMMON_ALLY_SUMMONS, data.summons());
        }
    }
}
