package de.peoples_magic.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.AetherGripPullsPayload;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class SetAetherGripPullsCommand {
    public SetAetherGripPullsCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("set_aether_grip_pulls")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("value", IntegerArgumentType.integer())
                        .executes(
                                (context) -> execute(context.getSource().getPlayer(),
                                        EntityArgument.getPlayer(context, "player"),
                                        IntegerArgumentType.getInteger(context, "value"))
                        ))));
    }

    private int execute(ServerPlayer source, ServerPlayer serverPlayer, int value){
        serverPlayer.setData(ModAttachments.AETHER_GRIP_PULLS, value);
        PacketDistributor.sendToPlayer(serverPlayer, new AetherGripPullsPayload(value));
        return Command.SINGLE_SUCCESS;
    }
}
