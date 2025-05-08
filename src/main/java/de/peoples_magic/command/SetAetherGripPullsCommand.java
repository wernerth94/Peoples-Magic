package de.peoples_magic.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.AetherGripPullsPayload;
import de.peoples_magic.payloads.sync.FireballHitPayload;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class SetAetherGripPullsCommand {
    public SetAetherGripPullsCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("set_aether_grip_pulls")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("value", IntegerArgumentType.integer())
                        .executes(
                                (context) -> execute(context.getSource().getPlayer(),
                                        IntegerArgumentType.getInteger(context, "value"))
                        )));
    }

    private int execute(ServerPlayer player, int value){
        player.setData(ModAttachments.AETHER_GRIP_PULLS, value);
        PacketDistributor.sendToPlayer(player, new AetherGripPullsPayload(value));
        return Command.SINGLE_SUCCESS;
    }
}
