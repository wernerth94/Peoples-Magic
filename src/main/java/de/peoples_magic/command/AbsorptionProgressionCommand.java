package de.peoples_magic.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import de.peoples_magic.Config;
import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;

import java.util.List;

public class AbsorptionProgressionCommand {
    public AbsorptionProgressionCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("absorption_progression")
                        .requires(source -> source.hasPermission(2))
                        .executes(
                                (context) -> absorption_prog(context.getSource().getPlayer())
                        ));
    }

    private int absorption_prog(ServerPlayer player){
        for (int i=0; i < Config.absorption_progression.size(); i++) {
            PeoplesMagicMod.LOGGER.info("Level: {} - Mitigation: {}", i, Config.absorption_progression.get(i));
        }
        return Command.SINGLE_SUCCESS;
    }
}
