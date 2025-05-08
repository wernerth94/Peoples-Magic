package de.peoples_magic.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.AbsorptionMitigationPayload;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class SetAbsorptionMitigationCommand {
    public SetAbsorptionMitigationCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("set_absorption_mitigation")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("value", IntegerArgumentType.integer())
                        .executes(
                                (context) -> set_mitigation(context.getSource().getPlayer(),
                                        IntegerArgumentType.getInteger(context, "value"))
                        )));
    }

    private int set_mitigation(ServerPlayer player, int value){
        player.setData(ModAttachments.ABSORPTION_MITIGATION, (float)value);
        PacketDistributor.sendToPlayer(player, new AbsorptionMitigationPayload(value));
        return Command.SINGLE_SUCCESS;
    }
}
