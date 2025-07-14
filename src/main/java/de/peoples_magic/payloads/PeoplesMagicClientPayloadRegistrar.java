package de.peoples_magic.payloads;

import de.peoples_magic.payloads.spells.*;
import de.peoples_magic.payloads.spells.handlers.*;
import de.peoples_magic.payloads.sync.*;
import de.peoples_magic.payloads.sync.handlers.*;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PeoplesMagicClientPayloadRegistrar {

    public static void register(RegisterClientPayloadHandlersEvent event) {
        register_spell_casts(event);
        register_spell_data(event);
        register_other_data(event);
    }

    private static void register_spell_casts(RegisterClientPayloadHandlersEvent event) {

    }

    private static void register_spell_data(RegisterClientPayloadHandlersEvent event) {


    }

    private static void register_other_data(RegisterClientPayloadHandlersEvent event) {

        event.register(
                UpdateKnowledgePayload.TYPE,
                new MainThreadPayloadHandler<>(UpdateKnowledgeClientHandler::handleDataOnMain)
        );

    }
}
