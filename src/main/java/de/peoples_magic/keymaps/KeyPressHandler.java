package de.peoples_magic.keymaps;

import de.peoples_magic.payloads.spells.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import static de.peoples_magic.keymaps.PeoplesMagicKeyMaps.*;

public class KeyPressHandler {

    // Event is on the NeoForge event bus only on the physical client
    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        while (SPELL_ABSORPTION_MAPPING.get().consumeClick()) {
            ClientPacketDistributor.sendToServer(new CastAbsorptionPayload(-1));
        }
        while (SPELL_REPEL_MAPPING.get().consumeClick()) {
            ClientPacketDistributor.sendToServer(new CastRepelPayload(-1));
        }
        while (SPELL_FIREBALL_MAPPING.get().consumeClick()) {
            ClientPacketDistributor.sendToServer(new CastFireballPayload(-1));
        }
        while (SPELL_ICE_CONE_MAPPING.get().consumeClick()) {
            ClientPacketDistributor.sendToServer(new CastIceconePayload(-1));
        }
        while (SPELL_AETHER_GRIP_MAPPING.get().consumeClick()) {
            ClientPacketDistributor.sendToServer(new CastAetherGripPayload(-1));
        }
        while (SPELL_HASTE_MAPPING.get().consumeClick()) {
            ClientPacketDistributor.sendToServer(new CastHastePayload(-1));
        }
        while (SPELL_FARMING_MAPPING.get().consumeClick()) {
            ClientPacketDistributor.sendToServer(new CastFarmingPayload(-1));
        }
        while (SPELL_SUMMON_ALLY_MAPPING.get().consumeClick()) {
            ClientPacketDistributor.sendToServer(new CastSummonAllyPayload(-1));
        }
    }
}
