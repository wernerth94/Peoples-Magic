package de.peoples_magic.payloads.sync.handlers;

import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.FarmingAnimalsBredPayload;
import de.peoples_magic.payloads.sync.FarmingIsActivePayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class FarmingAnimalsBredClientHandler {

    public static void handleDataOnMain(final FarmingAnimalsBredPayload data, final IPayloadContext context) {
        if (context.player().level().isClientSide) {
            context.player().setData(ModAttachments.FARMING_ANIMALS_BRED, data.animals());
        }
    }
}
