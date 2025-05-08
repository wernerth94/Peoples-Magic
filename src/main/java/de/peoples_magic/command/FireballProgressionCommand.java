package de.peoples_magic.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import de.peoples_magic.Config;
import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.Util;
import de.peoples_magic.attachments.ModAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;

import java.util.List;

public class FireballProgressionCommand {
    public FireballProgressionCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("fireball_progression")
                        .requires(source -> source.hasPermission(2))
                        .executes(
                                (context) -> fireball_progression(context.getSource().getPlayer())
                        ));
    }

    private int fireball_progression(ServerPlayer player){
        for (int i = 0; i < Config.fireball_progression.size(); i++) {
            PeoplesMagicMod.LOGGER.info("Level: {} - Direct Hits: {}", i, Config.fireball_progression.get(i));
        }
        return Command.SINGLE_SUCCESS;
    }
}
