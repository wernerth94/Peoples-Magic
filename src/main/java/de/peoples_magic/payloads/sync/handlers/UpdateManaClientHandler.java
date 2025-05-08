package de.peoples_magic.payloads.sync.handlers;

import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.UpdateManaPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class UpdateManaClientHandler {

    public static void handleDataOnMain(final UpdateManaPayload data, final IPayloadContext context) {
        if (context.player().level().isClientSide) {
            context.player().setData(ModAttachments.PLAYER_MANA, data.mana());
        }
    }
}
