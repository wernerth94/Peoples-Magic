package de.peoples_magic.payloads.sync.handlers;

import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.UpdateCooldownsPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Objects;

public class UpdateCooldownsClientHandler {

    public static void handleDataOnMain(final UpdateCooldownsPayload data, final IPayloadContext context) {
        if (context.player().level().isClientSide) {
            if (Objects.equals(data.name(), "absorption")) {
                context.player().setData(ModAttachments.ABSORPTION_ACTIVE_CD.get(), data.cd());
            }
            else if (Objects.equals(data.name(), "repel")) {
                context.player().setData(ModAttachments.REPEL_ACTIVE_CD.get(), data.cd());
            }
            else if (Objects.equals(data.name(), "fireball")) {
                context.player().setData(ModAttachments.FIREBALL_ACTIVE_CD.get(), data.cd());
            }
            else if (Objects.equals(data.name(), "ice_cone")) {
                context.player().setData(ModAttachments.ICE_CONE_ACTIVE_CD.get(), data.cd());
            }
            else if (Objects.equals(data.name(), "aether_grip")) {
                context.player().setData(ModAttachments.AETHER_GRIP_ACTIVE_CD.get(), data.cd());
            }
            else if (Objects.equals(data.name(), "haste")) {
                context.player().setData(ModAttachments.HASTE_ACTIVE_CD.get(), data.cd());
            }
            else if (Objects.equals(data.name(), "farming")) {
                context.player().setData(ModAttachments.FARMING_ACTIVE_CD.get(), data.cd());
            }
            else if (Objects.equals(data.name(), "summon_ally")) {
                context.player().setData(ModAttachments.SUMMON_ALLY_ACTIVE_CD.get(), data.cd());
            }
            else {
                throw new RuntimeException(String.format("Spell not recognized %s", data.name()));
            }






        }
    }
}
