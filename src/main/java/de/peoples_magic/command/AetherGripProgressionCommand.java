package de.peoples_magic.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import de.peoples_magic.Config;
import de.peoples_magic.PeoplesMagicMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class AetherGripProgressionCommand {
    public AetherGripProgressionCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("aether_grip_progression")
                        .requires(source -> source.hasPermission(2))
                        .executes(
                                (context) -> execute(context.getSource().getPlayer())
                        ));
    }

    private int execute(ServerPlayer player){
        for (int i = 0; i < Config.aether_grip_progression.size(); i++) {
            PeoplesMagicMod.LOGGER.info("Level: {} - Skeletons Pulled: {}", i, Config.aether_grip_progression.get(i));
        }
        return Command.SINGLE_SUCCESS;
    }
}
