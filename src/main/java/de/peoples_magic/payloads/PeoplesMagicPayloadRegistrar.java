package de.peoples_magic.payloads;

import de.peoples_magic.payloads.spells.*;
import de.peoples_magic.payloads.spells.handlers.*;
import de.peoples_magic.payloads.sync.*;
import de.peoples_magic.payloads.sync.handlers.*;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PeoplesMagicPayloadRegistrar {

    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        register_spell_casts(registrar);
        register_spell_data(registrar);
        register_other_data(registrar);
    }

    private static void register_spell_casts(PayloadRegistrar registrar) {

        registrar.playToServer(
                CastAbsorptionPayload.TYPE,
                CastAbsorptionPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(SpellAbsorptionServerHandler::handleDataOnMain)
        );

        registrar.playToServer(
                CastRepelPayload.TYPE,
                CastRepelPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(SpellRepelServerHandler::handleDataOnMain)
        );

        registrar.playToServer(
                CastFireballPayload.TYPE,
                CastFireballPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(SpellFireballServerHandler::handleDataOnMain)
        );

        registrar.playToServer(
                CastIceconePayload.TYPE,
                CastIceconePayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(SpellIceConeServerHandler::handleDataOnMain)
        );

        registrar.playToServer(
                CastAetherGripPayload.TYPE,
                CastAetherGripPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(SpellAetherGripServerHandler::handleDataOnMain)
        );

        registrar.playToServer(
                CastHastePayload.TYPE,
                CastHastePayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(SpellHasteServerHandler::handleDataOnMain)
        );

        registrar.playToServer(
                CastFarmingPayload.TYPE,
                CastFarmingPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(SpellFarmingServerHandler::handleDataOnMain)
        );

        registrar.playToServer(
                CastSummonAllyPayload.TYPE,
                CastSummonAllyPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(SpellSummonAllyServerHandler::handleDataOnMain)
        );
    }

    private static void register_spell_data(PayloadRegistrar registrar) {

        registrar.playToClient(
                AbsorptionMitigationPayload.TYPE,
                AbsorptionMitigationPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(AbsorptionMitigationClientHandler::handleDataOnMain)
        );

        registrar.playToClient(
                RepelSpidersPayload.TYPE,
                RepelSpidersPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(RepelSpidersClientHandler::handleDataOnMain)
        );

        registrar.playToClient(
                FireballHitPayload.TYPE,
                FireballHitPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(FireballHitClientHandler::handleDataOnMain)
        );

        registrar.playToClient(
                AetherGripPullsPayload.TYPE,
                AetherGripPullsPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(AetherGripPullsClientHandler::handleDataOnMain)
        );

        registrar.playToClient(
                AetherGripEntityPayload.TYPE,
                AetherGripEntityPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(AetherGripEntityClientHandler::handleDataOnMain)
        );

        registrar.playToClient(
                IceConeHitsPayload.TYPE,
                IceConeHitsPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(IceConeHitsClientHandler::handleDataOnMain)
        );

        registrar.playToClient(
                HasteIsActivePayload.TYPE,
                HasteIsActivePayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(HasteIsActiveClientHandler::handleDataOnMain)
        );

        registrar.playToClient(
                HasteUptimePayload.TYPE,
                HasteUptimePayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(HasteUptimeClientHandler::handleDataOnMain)
        );

        registrar.playToClient(
                FarmingIsActivePayload.TYPE,
                FarmingIsActivePayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(FarmingIsActiveClientHandler::handleDataOnMain)
        );

        registrar.playToClient(
                FarmingAnimalsBredPayload.TYPE,
                FarmingAnimalsBredPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(FarmingAnimalsBredClientHandler::handleDataOnMain)
        );

        registrar.playToClient(
                SummonAllySummonsPayload.TYPE,
                SummonAllySummonsPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(SummonAllySummonsClientHandler::handleDataOnMain)
        );
    }

    private static void register_other_data(PayloadRegistrar registrar) {

        registrar.playToClient(
                UpdateManaPayload.TYPE,
                UpdateManaPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(UpdateManaClientHandler::handleDataOnMain)
        );

        registrar.playToClient(
                UpdateCooldownsPayload.TYPE,
                UpdateCooldownsPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(UpdateCooldownsClientHandler::handleDataOnMain)
        );

        registrar.commonBidirectional(
                UpdateKnowledgePayload.TYPE,
                UpdateKnowledgePayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(UpdateKnowledgeClientHandler::handleDataOnMain)
        );

        registrar.playToClient(
                DisplayMessagePayload.TYPE,
                DisplayMessagePayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(DisplayMessageClientHandler::handleDataOnMain)
        );

        registrar.playToClient(
                NoManaPayload.TYPE,
                NoManaPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(NoManaClientHandler::handleDataOnMain)
        );

        registrar.playToClient(
                ActiveSpellCDPayload.TYPE,
                ActiveSpellCDPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(ActiveCDClientHandler::handleDataOnMain)
        );
    }
}
