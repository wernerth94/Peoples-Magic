package de.peoples_magic.payloads.sync.handlers;

import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.UpdateKnowledgePayload;
import de.peoples_magic.payloads.sync.UpdateManaPayload;
import net.minecraft.util.Tuple;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Map;

public class UpdateKnowledgeClientHandler {

    public static void handleDataOnMain(final UpdateKnowledgePayload data, final IPayloadContext context) {
        for (Map.Entry<String, AttachmentType<Integer>> entry : ModAttachments.all_spell_knowledge().entrySet()) {
            String name = entry.getKey();
            if (name.equals(data.spell())) {
                context.player().setData(entry.getValue(), data.knowledge());
                return;
            }
        }
        PeoplesMagicMod.LOGGER.error("[UpdateKnowledgeClientHandler] Spell name {} not recognized", data.spell());
    }
}
