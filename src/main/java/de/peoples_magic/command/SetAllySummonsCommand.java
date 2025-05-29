package de.peoples_magic.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.SummonAllySummonsPayload;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class SetAllySummonsCommand {
    public SetAllySummonsCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("set_summons_count")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("value", IntegerArgumentType.integer())
                        .executes(
                                (context) -> exec(context.getSource().getPlayer(),
                                        EntityArgument.getPlayer(context, "player"),
                                        IntegerArgumentType.getInteger(context, "value"))
                        ))));
    }

    private int exec(ServerPlayer source, ServerPlayer serverPlayer, int value){
        serverPlayer.setData(ModAttachments.SUMMON_ALLY_SUMMONS, value);
        PacketDistributor.sendToPlayer(serverPlayer, new SummonAllySummonsPayload(value));
        return Command.SINGLE_SUCCESS;
    }
}
