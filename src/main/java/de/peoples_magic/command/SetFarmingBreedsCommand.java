package de.peoples_magic.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import de.peoples_magic.attachments.ModAttachments;
import de.peoples_magic.payloads.sync.FarmingAnimalsBredPayload;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class SetFarmingBreedsCommand {
    public SetFarmingBreedsCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("set_farming_breeds")
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
        serverPlayer.setData(ModAttachments.FARMING_ANIMALS_BRED.get(), value);
        PacketDistributor.sendToPlayer(serverPlayer, new FarmingAnimalsBredPayload(value));
        return Command.SINGLE_SUCCESS;
    }
}
