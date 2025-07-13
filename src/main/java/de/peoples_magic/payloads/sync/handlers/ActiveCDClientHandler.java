package de.peoples_magic.payloads.sync.handlers;

import de.peoples_magic.overlays.LearnedSpellsOverlay;
import de.peoples_magic.payloads.sync.ActiveSpellCDPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ActiveCDClientHandler {

    public static void handleDataOnMain(final ActiveSpellCDPayload data, final IPayloadContext context) {
        if (context.player().level().isClientSide) {
            LearnedSpellsOverlay.instance.active_cd_flash(data.spell_name());
        }
    }
}
